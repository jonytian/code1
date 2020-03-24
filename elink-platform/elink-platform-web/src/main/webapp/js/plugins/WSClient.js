(function(window) {
	window.WSClient = function(option) {
		getWebSocket = function(host) {
			var websocket = null;
			// 判断当前浏览器是否支持WebSocket
			if ('WebSocket' in window) {
				websocket = new WebSocket(host)
			} else if ('MozWebSocket' in window) {
				websocket = new MozWebSocket(host)
			} else {
				alert('错误: 当前浏览器不支持WebSocket，请更换其他浏览器');
			}
			return websocket;
		},init = function(client, option) {
            client.socket = null;
            client.option = option || {};
        };

		this.connect = function(host) {
			host = host || this.option.host;
	        var client = this,
	        option = client.option,
			socket = getWebSocket(host);
			if (socket == null) {
				return
			}

			// 连接成功建立的回调方法
			socket.onopen = function() {
				var onopen = option.onopen;
				onopen && onopen()
			};

			// 连接关闭的回调方法
			socket.onclose = function() {
				var onclose = option.onclose;
				onclose && onclose();
			};

			// //接收到消息的回调方法
			socket.onmessage = function(message) {
				var onmessage = option.onmessage;
				onmessage && onmessage(message);
			};

			// 连接发生错误的回调方法
			socket.onerror = function() {
				var onerror = option.onerror;
				onerror && onerror();
			};

			this.socket = socket;
			return this
		};

		this.initialize = function(param) {
			return this.connect(this.option.host + (param ? "?" + param : ""))
		};

		this.send = function(message) {
			return this.socket.send(message)
		};

		this.close = function() {
			if (this.socket) {
				this.socket.close();
				this.socket = null;
			}
			return true;
		};
		init(this, option);
	}
})(window);