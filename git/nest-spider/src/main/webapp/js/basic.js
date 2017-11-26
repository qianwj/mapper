/*
 * Create by TYUT Software Engineer 1517 Qian
 * Copyright 2017 Nest Technology
 * Thanks for your coming and check
 */

function jsonStringify(data, space) {
    var arr = [];
    return JSON.stringify(data, function (key, val) {
        if (!val || typeof val !== 'object')
            return val;
        if (arr.indexOf(val) !== -1)
            return '[Circular]';
        arr.push(val);
        return val;
    }, space);
}

/*
 * 文件下载， file:文件名， content:文件内容
 */
function downloadFile(file, content) {
    var link = document.createElement('a');
    var blob = new Blob([content]);
    var evt = document.createEvent('HTMLEvents');
    evt.initEvent('click', false, false);
    link.download = file;
    link.href = URL.createObjectURL(blob);/*URL对象属于Web API*/
    link.dispatchEvent(evt);
}

function rand(min, max) {
    var range = max - min;
    var ran = Math.random();
    return min + Math.round(range*ran);
}

function isEnWebpage(content) {
    var times = 0, max_ = 15, threshold = .5;
    if(content.length<=0)
        return false;
    for(var i = 0;i<content.length;i++) {
        var char = content.charAt(rand(0, content.length));
        if((char>='a' && char<='z') || (char>='A' && char<='Z'))
            times++;
    }
    return times/max_ > threshold;
}