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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger("kafka-event");
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
        cache=Caffeine.newBuilder().expireAfterAccess(30, TimeUnit.MINUTES)
                .build();
    }

    @GetMapping("/log")
    public MessageResult log(@RequestParam("msg") String msg)  {
            log.info(msg);
        return MessageResult.success(msg);

    }

    @GetMapping("/sso/test")
    public MessageResult test(@RequestParam String token) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {

        return MessageResult.success("token");

    }


    @PostMapping("/sso/login")
    public MessageResult login(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String code,
    HttpServletResponse resp) throws NoSuchPaddingException, IOException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        System.out.println("登录");
        User user = userDao.getuserbyid(username);
        System.out.println(password+"    "+user.salt);
        if(password.isEmpty()||!MD5Util.getMD5Str(password+user.salt).equals(user.password)) {
            return MessageResult.error("密码不正确");
        }
        String token = MD5Util.getSalt(20);
        cache.put(token,username);
        System.out.println(token);
        resp.addCookie(new Cookie("loginCookie",token));
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

//1 0 0 0 1
//0 0 0 0 0
//0 0 1 0 1
//1 5 10  2,4,8.13
//
//1 8  4

    // 3 3 -8
    // 3 6 -2
    //   3 -5
    public static int a = 0;
    public static volatile int b = 0;  //去掉volatile修饰会发生死循环，即变量a对于其他线程不是立即可见

        public static void main(String[] args) throws InterruptedException {
//            ReentrantLock
            Thread thread = new Thread(() -> {
                while(a==0) {
                    int c = b;
                }
            });
            thread.start();

            Thread.sleep(200);

            new Thread(() -> {
                a = 1;
                b = 1;    //volatile可以将前面顺序的写入的变量也会刷入主存
            }).start();

            thread.join();
        }
     public static Set<String> set=new HashSet<>();
    public static List<String> removeInvalidParentheses(String s) {
           int dleft=0;
           int dright=0;
            for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='(') dleft++;
            else if(s.charAt(i)==')') {
                if (dleft==0) dright++;
                else dleft--;
            }

        }
            df(s,dleft,dright);
            return new ArrayList<>(set);
    }

    private static void df(String s, int dleft, int dright) {


        for (int i = 0; i <s.length() ; i++) {

            if(check(s)) set.add(s);

            char c=s.charAt(i);
            if(dleft+dright>s.length()-i) return;

            if(c=='('&&dleft>0){
                df(s.substring(0,i)+s.substring(i+1),dleft--,dright);
            }
            if(c==')'&&dright>0){
                df(s.substring(0,i)+s.substring(i+1),dleft,dright--);
            }






        }
    }

    private static boolean check(String s) {
        int left=0;
        int right=0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i)=='(')  left++;
            else if(s.charAt(i)==')') right++;
            if(right>left) return false;
        }
        return left==right;
    }

    public static void indexsort(int[] data, int n){

      int max= Arrays.stream(data).max().getAsInt();
      int l=String.valueOf(max).length();       //求得最大位数的长度
         int chu=1;

        for (int i = 0; i < l; i++) {
            int[] tong = new int[10];
            int[] tem=new int[n];
            for (int j = 0; j < n; j++) {
                int k=(data[j]/chu)%10;
                tong[k]++;
            }
            for (int j = 1; j < 10; j++) {
                tong[j]=tong[j]+tong[j-1];    //求和后刚好是n，刚好对应与data上的位置
            }
            for (int j = n-1; j >=0 ; j--) {
                int k=(data[j]/chu)%10;
                tem[tong[k]-1]=data[j];  //这个地方是从后往前排，所以先放大的，所以data数组逆序取
                tong[k]--;
            }
            data=tem;
             chu*=10;
        }
        System.out.println(data);
    }

    public static int minPatches(int[] nums, int n) {
      Set<Integer> set=new HashSet<>();
      int l=nums.length;
        for (int i = 0; i < nums.length; i++) {
            set.add(nums[i]);
        }
        for (int i = 1; i <1<<l ; i++) {
            int in=0;
            for (int j = 0; j <l ; j++) {
                int shu=1<<j;
                if((i&shu)>0)
                    in+=nums[j];

            }
            set.add(in);
        }
        int min=0;
        for (int i = 1; i <=n ; i++) {
            if(set.contains(i)) continue;
            Set<Integer> nset = new HashSet<>();
            for(int index:set){
                nset.add(index+i);
            }
            nset.add(i);
            nset.addAll(set);
            set=nset;
            nset=null;
            min++;
        }
        return min;
    }


         //1 0 0 1 1 0 0 1  两边到其中间之和为定值。
        //1 2 3 0 0 1 5 3 2  加上权重后
//求最小值，二分，如何判断往左还是往右
    public static int minTotalDistance(int[][] grid) {
        int[] col = new int[grid.length];
        int[] cow = new int[grid[0].length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                col[i] += grid[i][j];
                cow[j] += grid[i][j];
            }
        }
         return mind(cow)+mind(col);
    }
           public static int mind(int [] cow){
           int min=0;
           int left=0;
           int right=cow.length-1;
           while(left<right){
               if(cow[left]==0) {
                   left++;
                   continue;
               }
               if(cow[right]==0){
                   right--;
                   continue;
               }
              int cowmin=Math.min(cow[left],cow[right]);
               min+=(right-left)*cowmin;
               cow[left]-=cowmin;
               cow[right]-=cowmin;
           }
           return min;
    }
}
