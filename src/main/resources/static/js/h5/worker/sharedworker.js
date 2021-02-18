
//标识当共享子线程与主线程建立连时出发的事件
onconnect = function (ev) {
    var port0 = ev.ports[0];
    port0.postMessage("connect sharedwork success");
    port0.onmessage = onMsg;
};

function onMsg(ev) {
    var a = ev.data.a;
    var b = ev.data.b;
    var c = ev.data.c;
    // var worker = e.currentTarget;
    this.postMessage(self.navigator.appVersion);
    // this.postMessage(self.location.host);
    // self.close();
    // this.postMessage(self.location.host);
    // this.postMessage("a:" +a + "/b:" + b + "/c:" + c);
    // this.postMessage({
    //     a: a,
    //     b: b
    // });
}


/**
 * 网上demo 实现多网页通信

var textlist = [],
    connectList = [];
self.addEventListener('connect', function (e) {
    var port = e.ports[0]
    port.start();
    port.addEventListener('message', function (e) {
        // obj.target = e.currentTarget;
        var worker = e.currentTarget,
            res = e.data;
        if (connectList.indexOf(worker) === -1) {
            connectList.push(worker)
        }
        switch (res.status) {
            case 0:
                inform(function (item) {
                    if (item != worker) {
                        item.postMessage('有新用户加入');
                    } else {
                        item.postMessage('我是新用户');
                    }
                });
                break;

            default:
                textlist.push(res.data.value);
                inform(textlist);
                break;
        }
    })
});
// 分发消息
function inform(obj) {
    var cb = (typeof obj === 'function') ? obj : function (item) {
        item.postMessage(obj);
    }
    connectList.forEach(cb);
}
 */