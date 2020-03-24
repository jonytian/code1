function LocalStorage() {
	if(!window.localStorage){
		alert(alert("浏览器不支持localstorage"));
		return false;
	}
	this.localStorage = window.localStorage;

	this.put = function(key, value) {
		try{
			this.localStorage.setItem(key,value);
			}catch(oException){
				if(oException.name == 'QuotaExceededError'){
					console.log('已经超出本地存储限定大小！');
					// 可进行超出限定大小之后的操作，如下面可以先清除记录，再次保存
					this.localStorage.clear();
					this.localStorage.setItem(key,value);
				}
			}
	}
	this.get = function(key) {
		return this.localStorage.getItem(key);
	}
}