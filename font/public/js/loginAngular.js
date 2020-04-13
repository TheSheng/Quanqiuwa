var myApp = angular.module('login', ['ngCookies'])

myApp.controller('loginController', ['$scope', '$http','$cookies','$cookieStore',function ($scope, $http,$cookies,$cookieStore) {
    var vm = $scope

    request=$http
    vm.register=function () {
        if(vm.username.length<6||vm.username.length>15){
            swal("错误","用户名长度要在6到15位之间","error")
            return
        }
        if(vm.password.length<6||vm.password.length>15){
            swal("错误","密码长度要在6到15位之间","error")
            return
        }
        console.log(vm.password)
        data={
            username:vm.username,
            password:vm.password
        }
        console.log(data)
        request({
            method: 'POST',
            url: 'http://localhost:8080/user/register',
            data: data



        }).then(function (response) {
             console.log(response.data)

            if(response.data.code!=200){
                swal("错误",response.data.message,"error")
                return
            }
            swal("OK!", "注册成功，请登录", "success");
        }, function (response) {
        });


    }
    function tiao(url){
        window.location.href=url
    }
    vm.login=function () {
        if(vm.username.length<6||vm.username.length>15){
            swal("错误","用户名长度要在6到15位之间","error")
            return
        }
        if(vm.password.length<6||vm.password.length>15){
            swal("错误","密码长度要在6到15位之间","error")
            return
        }
        console.log(vm.password)
        data = {
            username: vm.username,
            password: vm.password,

        }
        console.log(data)
        request({
            method: 'POST',
            url: 'http://localhost:8080/user/login',
            data: data


        }).then(function (response) {
            // console.log(response.data)
            var rs=response.data
            console.log(response)
            if(rs.code=="200"){
                $cookieStore.put("user","ok")

                tiao("/")
            }else{
                swal("错误!", "用户名或密码错误", "error");
            }
            // console.log(response)
            // response.data.


        }, function (response) {

        });
    }


}])
