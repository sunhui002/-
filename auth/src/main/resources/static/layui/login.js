
layui.config({
    base : "js/"
}).use(['form','layer'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        $ = layui.jquery;
    //video背景
    // $(window).resize(function(){
    //     if($(".video-player").width() > $(window).width()){
    //         $(".video-player").css({"height":$(window).height(),"width":"auto","left":-($(".video-player").width()-$(window).width())/2});
    //     }else{
    //         $(".video-player").css({"width":$(window).width(),"height":"auto","left":-($(".video-player").width()-$(window).width())/2});
    //     }
    // }).resize();
    //rsa非对称算法加密
    var pubkey="MIICXQIBAAKBgQDlOJu6TyygqxfWT7eLtGDwajtNFOb9I5XRb6khyfD1Yt3YiCgQ\n" +
        "WMNW649887VGJiGr/L5i2osbl8C9+WJTeucF+S76xFxdU6jE0NQ+Z+zEdhUTooNR\n" +
        "aY5nZiu5PgDB0ED/ZKBUSLKL7eibMxZtMlUDHjm4gwQco1KRMDSmXSMkDwIDAQAB\n" +
        "AoGAfY9LpnuWK5Bs50UVep5c93SJdUi82u7yMx4iHFMc/Z2hfenfYEzu+57fI4fv\n" +
        "xTQ//5DbzRR/XKb8ulNv6+CHyPF31xk7YOBfkGI8qjLoq06V+FyBfDSwL8KbLyeH\n" +
        "m7KUZnLNQbk8yGLzB3iYKkRHlmUanQGaNMIJziWOkN+N9dECQQD0ONYRNZeuM8zd\n" +
        "8XJTSdcIX4a3gy3GGCJxOzv16XHxD03GW6UNLmfPwenKu+cdrQeaqEixrCejXdAF\n" +
        "z/7+BSMpAkEA8EaSOeP5Xr3ZrbiKzi6TGMwHMvC7HdJxaBJbVRfApFrE0/mPwmP5\n" +
        "rN7QwjrMY+0+AbXcm8mRQyQ1+IGEembsdwJBAN6az8Rv7QnD/YBvi52POIlRSSIM\n" +
        "V7SwWvSK4WSMnGb1ZBbhgdg57DXaspcwHsFV7hByQ5BvMtIduHcT14ECfcECQATe\n" +
        "aTgjFnqE/lQ22Rk0eGaYO80cc643BXVGafNfd9fcvwBMnk0iGX0XRsOozVt5Azil\n" +
        "psLBYuApa66NcVHJpCECQQDTjI2AQhFc1yRnCU/YgDnSpJVm1nASoRUnU8Jfm3Oz\n" +
        "uku7JUXcVpt08DFSceCEX9unCuMcT72rAQlLpdZir876"
    var encrypt = new JSEncrypt();
    encrypt.setPublicKey(pubkey);

    //登录按钮事件
    form.on("submit(login)",function(data){
        var userInfo = data.field;
        var url = "api/sso/login";
        $.ajax({
            url:url,
            type:'post',
            data: {username: userInfo.username,
                   password: encrypt.encrypt(userInfo.password),
                   code: userInfo.code,
                  },
            dataType : "json",
            // beforeSend:function () {
            //     this.layerIndex = layer.load(0, { shade: [0.5, '#393D49'] });
            // },
            success:function(oJson){
                if(!oJson.Result){
                    layer.msg(data.msg,{icon: 5});//失败的表情
                    return;
                }else {
                    localStorage.setItem('token',data.Result);
                    window.location = 'admin.html';
                    // layer.msg(data.msg, {
                    //     icon: 6,//成功的表情
                    //     time: 1000 //1秒关闭（如果不配置，默认是3秒）
                    // }, function(){
                    //     location.reload();
                    // });
                }
            },
            complete: function () {
                layer.close(this.layerIndex);
            },
        });
        // window.location = 'admin.html';
        return false;
    })
})