package com.example.auth.utils;




import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *  @Description: 中研通用加密工具 RSA+ASE+SHA256(非对称加密（对称秘钥），对称加密数据，Sha256消息摘要，RSA签名)
 *  @author  wh.huang  DateTime 2018年11月15日 下午3:00:21
 *  @version 1.0
 */
public class CsoftSecurityUtil {

    // 加密数据和秘钥的编码方式
    public static final String UTF_8 = "UTF-8";

    // 填充方式
    public static final String AES_ALGORITHM = "AES/CFB/PKCS5Padding";
    public static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String RSA_ALGORITHM_NOPADDING = "RSA";

    /**
     *  Description: 解密接收数据
     *  @author  wh.huang  DateTime 2018年11月15日 下午5:06:42
     *  @param externalPublicKey
     *  @param selfPrivateKey
     *  @param
     *  @throws InvalidKeyException
     *  @throws NoSuchPaddingException
     *  @throws NoSuchAlgorithmException
     *  @throws BadPaddingException
     *  @throws IllegalBlockSizeException
     *  @throws UnsupportedEncodingException
     *  @throws InvalidAlgorithmParameterException
     *  @throws DecoderException
     */
    public static String decryptReceivedData(PublicKey externalPublicKey, PrivateKey selfPrivateKey, String receiveData) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException, DecoderException {

        @SuppressWarnings("unchecked")
        Map<String, String> receivedMap = (Map<String, String>) JSON.parse(receiveData);

        // receivedMap为请求方通过from urlencoded方式，请求过来的参数列表
        String inputSign = receivedMap.get("sign");

        // 用请求方提供的公钥验签，能配对sign，说明来源正确
        inputSign = decryptRSA(externalPublicKey, inputSign);

        // 校验sign是否一致
        String sign = sha256(receivedMap);
        if (!sign.equals(inputSign)) {
            // sign校验不通过，说明双方发送出的数据和对方收到的数据不一致
            System.out.println("input sign: " + inputSign + ", calculated sign: " + sign);
            return null;
        }

        // 解密请求方在发送请求时，加密data字段所用的对称加密密钥
        String key = receivedMap.get("key");
        String salt = receivedMap.get("salt");
        key = decryptRSA(selfPrivateKey, key);
        salt = decryptRSA(selfPrivateKey, salt);

        // 解密data数据
        String data = decryptAES(key, salt, receivedMap.get("data"));
        System.out.println("接收到的data内容：" + data);
        return data;
    }

    /**
     *  Description: 加密数据组织示例
     *  @author  wh.huang DateTime 2018年11月15日 下午5:20:11
     *  @param externalPublicKey
     *  @param selfPrivateKey
     *  @return 加密后的待发送数据
     *  @throws NoSuchAlgorithmException
     *  @throws InvalidKeySpecException
     *  @throws InvalidKeyException
     *  @throws NoSuchPaddingException
     *  @throws UnsupportedEncodingException
     *  @throws BadPaddingException
     *  @throws IllegalBlockSizeException
     *  @throws InvalidAlgorithmParameterException
     */
    public static String encryptSendData(PublicKey externalPublicKey, PrivateKey selfPrivateKey,JSONObject sendData) throws NoSuchAlgorithmException, InvalidKeySpecException,
            InvalidKeyException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException,
            IllegalBlockSizeException, InvalidAlgorithmParameterException {

        // 随机生成对称加密的密钥和IV (IV就是加盐的概念，加密的偏移量)
        String aesKeyWithBase64 = genRandomAesSecretKey();
        String aesIVWithBase64 = genRandomIV();

        // 用接收方提供的公钥加密key和salt，接收方会用对应的私钥解密
        String key = encryptRSA(externalPublicKey, aesKeyWithBase64);
        String salt = encryptRSA(externalPublicKey, aesIVWithBase64);

        // 组织业务数据信息，并用上面生成的对称加密的密钥和IV进行加密
        System.out.println("发送的data内容：" + sendData.toJSONString());
        String cipherData = encryptAES(aesKeyWithBase64, aesIVWithBase64, sendData.toJSONString());

        // 组织请求的key、value对
        Map<String, String> requestMap = new TreeMap<String, String>();
        requestMap.put("key", key);
        requestMap.put("salt", salt);
        requestMap.put("data", cipherData);
        requestMap.put("source", "由接收方提供"); // 添加来源标识

        // 计算sign，并用请求方的私钥加签，接收方会用请求方发放的公钥验签
        String sign = sha256(requestMap);
        requestMap.put("sign", encryptRSA(selfPrivateKey, sign));

        // TODO: 以form urlencoded方式调用，参数为上面组织出来的requestMap

        // 注意：请务必以form urlencoded方式，否则base64转码后的个别字符可能会被转成空格，对方接收后将无法正常处理
        JSONObject json = new JSONObject();
        json.putAll(requestMap);
        return json.toString();
    }

    /**
     *  Description: 获取随机的对称加密的密钥
     *  @author  wh.huang  DateTime 2018年11月15日 下午5:25:53
     *  @return  对称秘钥字符
     *  @throws NoSuchAlgorithmException
     *  @throws UnsupportedEncodingException
     *  @throws IllegalBlockSizeException
     *  @throws BadPaddingException
     *  @throws InvalidKeyException
     *  @throws NoSuchPaddingException
     */
    private static String genRandomAesSecretKey() throws NoSuchAlgorithmException, UnsupportedEncodingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException, NoSuchPaddingException {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();
        String keyWithBase64 = Base64.encodeBase64(secretKey.getEncoded()).toString();

        return keyWithBase64;

    }

    private static String genRandomIV() {
        SecureRandom r = new SecureRandom();
        byte[] iv = new byte[16];
        r.nextBytes(iv);
        String ivParam = Base64.encodeBase64(iv)+"";
        return ivParam;
    }

    /**
     * 对称加密数据
     *
     * @param keyWithBase64
     * @param
     * @param plainText
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    private static String encryptAES(String keyWithBase64, String ivWithBase64, String plainText)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        byte[] keyWithBase64Arry = keyWithBase64.getBytes();
        byte[] ivWithBase64Arry = ivWithBase64.getBytes();
        SecretKeySpec key = new SecretKeySpec(Base64.decodeBase64(keyWithBase64Arry), "AES");
        IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(ivWithBase64Arry));

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        return Base64.encodeBase64(cipher.doFinal(plainText.getBytes(UTF_8))).toString();
    }

    /**
     * 对称解密数据
     *
     * @param keyWithBase64
     * @param cipherText
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    private static String decryptAES(String keyWithBase64, String ivWithBase64, String cipherText)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException,
            BadPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        byte[] keyWithBase64Arry = keyWithBase64.getBytes();
        byte[] ivWithBase64Arry = ivWithBase64.getBytes();
        byte[] cipherTextArry = cipherText.getBytes();
        SecretKeySpec key = new SecretKeySpec(Base64.decodeBase64(keyWithBase64Arry), "AES");
        IvParameterSpec iv = new IvParameterSpec(Base64.decodeBase64(ivWithBase64Arry));

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return new String(cipher.doFinal(Base64.decodeBase64(cipherTextArry)), UTF_8);
    }

    /**
     * 非对称加密，根据公钥和原始内容产生加密内容
     *
     * @param key
     * @param
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws UnsupportedEncodingException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws InvalidAlgorithmParameterException
     */
    private static String encryptRSA(Key key, String plainText)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return Base64.encodeBase64(cipher.doFinal(plainText.getBytes(UTF_8))).toString();
    }

    /**
     * 根据私钥和加密内容产生原始内容
     * @param key
     * @param content
     * @return
     * @throws NoSuchPaddingException
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws DecoderException
     * @throws BadPaddingException
     * @throws IllegalBlockSizeException
     * @throws UnsupportedEncodingException
     * @throws InvalidAlgorithmParameterException
     */
    private static String decryptRSA(Key key, String content) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, DecoderException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] contentArry = content.getBytes();
        return new String(cipher.doFinal(Base64.decodeBase64(contentArry)), UTF_8);
    }

    /**
     * 计算sha256值
     *
     * @param paramMap
     * @return 签名后的所有数据，原始数据+签名
     */
    private static String sha256(Map<String, String> paramMap) {
        Map<String, String> params = new TreeMap<String, String>(paramMap);

        StringBuilder concatStr = new StringBuilder();
        for (Entry<String, String> entry : params.entrySet()) {
            if ("sign".equals(entry.getKey())) {
                continue;
            }
            concatStr.append(entry.getKey() + "=" + entry.getValue() + "&");
        }

        return DigestUtils.md5Hex(concatStr.toString());
    }

    /**
     * 创建RSA的公钥和私钥示例 将生成的公钥和私钥用Base64编码后打印出来
     * @throws NoSuchAlgorithmException
     */
    public static void createKeyPairs() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048,new SecureRandom());
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        System.out.println("公钥"+ Base64.encodeBase64String(publicKey.getEncoded()));
        System.out.println("私钥"+ Base64.encodeBase64String(privateKey.getEncoded()));
    }

    /**
     *  Description:默认的RSA解密方法 一般用来解密 参数 小数据
     *  @author  wh.huang  DateTime 2018年12月14日 下午3:43:11
     *  @param privateKeyStr
     *  @param data
     *  @return
     *  @throws NoSuchAlgorithmException
     *  @throws InvalidKeySpecException
     *  @throws NoSuchPaddingException
     *  @throws InvalidKeyException
     *  @throws IllegalBlockSizeException
     *  @throws BadPaddingException
     */
    public static String decryptRSADefault(String privateKeyStr,String data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM_NOPADDING);
        byte[] privateKeyArray = privateKeyStr.getBytes();
        byte[] dataArray = data.getBytes();
        PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKeyArray));
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);

        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(Base64.decodeBase64(dataArray)), UTF_8);
    }
    public static String testDecrypt(String key,String data) throws NoSuchAlgorithmException, InvalidKeyException, NoSuchPaddingException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, IOException{
        byte[] decode = Base64.decodeBase64(key);
//		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(decode); //java底层 RSA公钥只支持X509EncodedKeySpec这种格式
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(decode);
        KeyFactory kf = KeyFactory.getInstance(RSA_ALGORITHM_NOPADDING);
        PublicKey generatePublic = kf.generatePublic(x509EncodedKeySpec);
        Cipher ci = Cipher.getInstance(RSA_ALGORITHM_NOPADDING);
        ci.init(Cipher.DECRYPT_MODE,generatePublic);

        byte[] bytes = Base64.decodeBase64(data);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        while(inputLen - offLen > 0){
            byte[] cache;
            if(inputLen - offLen > 128){
                cache = ci.doFinal(bytes,offLen,128);
            }else{
                cache = ci.doFinal(bytes,offLen,inputLen - offLen);
            }
            byteArrayOutputStream.write(cache);
            i++;
            offLen = 128 * i;

        }
        byteArrayOutputStream.close();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new String(byteArray);
    }

}
