<?php
	/*
	 *手机登录接口
	 *@author zhengwei
	 */
	
/*	require_once 'UserService.class.php';
	$loginname=$_POST['username'];
	$password=$_POST['password'];
	$userService=new UserService();
	
	if($name=$userService->checkUser($loginname,$password)){
		echo "登录成功";
	}
	else{
		echo "用户名或密码错误！";
	}
	*/
	//header('Content-type:text/json');     //这句是重点，它告诉接收数据的对象此页面输出的是json数据；
	
	$token=$_POST['token'];
	$hui=$_POST['hui'];
	$timestamp=$_POST['timestamp'];
	$verifycode=$_POST['verifycode'];
	$loginname=$_POST['loginname'];
	$password=$_POST['password']; 
	
	$staus='status';
	$msg='msg';
	//php中用数组表示JSON格式数据
	$arr = array(
	    'staus' => $staus,
	    'msg' => $msg,
		'username' => $timestamp,
		'password' => $password
	);
	//将数组编码成JSON数据格式
	$json_string = json_encode($arr);
	//JSON格式数据可直接输出
	echo $json_string;
	
?>