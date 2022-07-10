package com.example.auth.controller;


import com.example.auth.Dao.MenuDao;
import com.example.auth.Dao.RoleDao;
import com.example.auth.Dao.UserDao;
import com.example.auth.Entity.Menu;
import com.example.auth.Entity.MessageResult;
import com.example.auth.Entity.User;
import com.example.auth.utils.MD5Util;
import com.example.auth.utils.CsoftSecurityUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class LoginController {
      @Autowired
    UserDao userDao;
//    loginid: wcadmin
//    password: c4ca4238a0b923820dcc509a6f75849b
//    verifykey: c1f516bd-1e93-8917-e3f2-8eac594b0419
//    verifycode: 6666
//    autologin: 0
    //首位不需要begin和end。
    private String privatekey=
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCCPZUUrgZpXB1TzxQJbGK9HHUFWlRGqByreBh7zGQUWndhsaMRGlNB8pHf6asyGO8SNPAKDowCLCr6MYadzjvIT7xsBVmZMe/lvAACBujAcZhHtff/xnxheeUbTFQ3QyZeFkPTz+b11oiGsuwk/DNUQDiI3uwSaLrUoN/4fXxGtFdbFHoB8vhEEs/c3m64T+U804nfeWy1KTTuOnaC+cgD3vUeMprp/q8KvuIUha6H/RH0DxNCmNl3M1aaHV0Qwz1QXIy0bOGNWK2FUQrpX3NfAUljsnMtnXjkxR91PlpN/rATbp9aiKR7lkMdj1JjBtdg6r5W2tTait0//4fuAfUhAgMBAAECggEAMW6CKMksMcz+bJLJ/hGfIDuuxotUNUUVLJd2Y3Zd/cOgT/oF9DXTp9ITP7MGgQspofVprcd7qE9vOGqXt0lRM2PgFfep3BUyAjXqotpQeMokjAMqz29cQfrr1AKSbC8WXtuAfuIHvQLh6o36RsdaC/4b9wb0/r+1OYKdv0iRZJv75RCDXRaItSmhIgQSIKnu0aDadm7bFZ8eHpY8ZRqhUAPPzn7gbuDzXCTQfuH8wnJiLA8y4y9T5uPne8+up5dBOG66nHiDKiAFEy/99J4h4np/z0+1Cx4KBL17DRndkBLiHNjb/5fRjiUqiYzNe9pVoLkWbyO3BONgV9NwugDxKQKBgQDlCrn3dRDaInL0IkbDhBl9so+oKLDIhickwT1Rv9n9AD64UZu3b2BnduNESif0jbe56rVtI/+F+vWE1cUf1aKsFGdjnibNfjsQJ+3Z/rOenE3mKsuCC1DPMnCWcqYWfn8qjBCTDtZoejncSeJ8HmK1CSe4UV9JvvqbCytY9tpkTwKBgQCRkd4OZn7itY5rvIPTFnKjR1SIcxyGaiSgPpBOqCtl4S2BbBX2isMkgx8al8Qs6asqUML8m8Q2IXLW2qU8+IoslTrYp2sx0QsjhGc7/AW8V4mulNBBR1GHj4yBKWgCf7tGN0lfjWF+PoIASre8WLELpg4UnOpn4fdUTsQ+9vkDjwKBgQCcfSSsTx6xe0IBTX39zb7A5ARFB3ApwYbzFNh5h1RACS/RLWjv/rfmWRNUMDp2W3GBJDzLEgJenyWqmYfyhTt8yQAOzxqB4lImarGW9O/yWabbsLeU5XhT6ImGMVULH2hxQHt+jm+tuHzZ5t1IolYcjOHHjElHkKWW0x3NzfNO/wKBgC/zcMYllR5w1909V98swc5vLLVV1cuhIzd15GtEkicZKbg8Nm/j/awFVTWZUJHK1ws1V0WGufdqxT1pAI05Aar17VB4taLPTXABqU/lEK6d10ie1IrsRzr0WkAht5U7JiFXDOBg3gCL7p7R/fwY6xj/NhwsJDrcAHDREWv8RsKZAoGALntf5FTk4TMVCL/+6++PBT8kXZK+qLFTOgPuSEzw7qRXFapWPeihNi2hAMCVRxIak6SnPezSPNesHohjFYBm+Bi6KxT2Rvn5uryS/8RdhPZq6p3Q8y1g/1kcBTAdG0vCE3jzXkdJ0KC3wzqj8hG3X+fsUyWuUR8IhTU/+aCx+u8=";

    public  static Cache<String, String> cache;
    static {
        cache=Caffeine.newBuilder().expireAfterAccess(10, TimeUnit.SECONDS)
                .build();
    }

    @PostMapping("/sso/login")
    public MessageResult login(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String code
    ) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        System.out.println("登录");
        User user = userDao.getuserbyid(username);
        System.out.println(password+"    "+user.salt);
        if(password.isEmpty()||!MD5Util.getMD5Str(password+user.salt).equals(user.password)) {
            return MessageResult.error("密码不正确");
        }
        String token = MD5Util.getSalt(20);
        cache.put(token,username);
        return MessageResult.success(token);

    }
    @PostMapping("/sso/register")
    public MessageResult register(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String code
    ) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        System.out.println("注册");
        String salt = MD5Util.getSalt(10);
        String newpassword = MD5Util.getMD5Str(password + salt);
        System.out.println(password+"    "+salt);
        User user = new User();
        user.setUsername(username);
        user.setSalt(salt);
        user.setPassword(newpassword);
        int saveuser = userDao.saveuser(user);
       if(saveuser>0) return MessageResult.Ok(true);
        return MessageResult.error("注册失败");

    }
    @Autowired
    RoleDao roleDao;
    @Autowired
    MenuDao menuDao;
//    ticket: nihao
//    envtype: 2
    @GetMapping("/account/getClientData")
    public MessageResult getClientData(@RequestParam String token){

        System.out.println("正在登录中");

        List<String> system = roleDao.getmenusbyrole("system");
        List<Menu> selectbymenuguid = menuDao.selectbymenuguid(system);
        Map<String,Menu> Mmap=new HashMap<>();
        selectbymenuguid.stream().forEach((m)->Mmap.put(m.Menuguid,m));
        List<Menu> subs=new ArrayList<>();
        for (Menu m:selectbymenuguid){
            m.setRunClassName("");
            if(m.type==0) subs.add(m);
            else {
                Menu parent = Mmap.get(m.parentguid);
                if (parent.SubNodes==null) {
                    parent.setSubNodes(new ArrayList<>());

                }
                parent.SubNodes.add(m);
            }
        }

        User user = new User();
        user.setSubNodes(subs);
        user.setUsername("w");
         Map<String,Object> map=new HashMap<>();
         map.put("user",user);
        Menu menu = new Menu();
        menu.setSubNodes(subs);
//        map.put("nav",menu);
         subs.remove(subs.size() - 1);
        return MessageResult.success(subs);

    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String RunUrl="你好.html";
        String Caption="准备";
        String md5Str1 = MD5Util.getMD5Str("1" + "mGCV4uO03R");
        String md5Str2 = MD5Util.getMD5Str("1" + "mGCV4uO03R");
        System.out.println(md5Str1);
        System.out.println(md5Str2);
        System.out.println("select spu_id, spu_name,spu_price,spu_img,spu_description from spu where spu_description like '%${decription}%' ");
    }
}
