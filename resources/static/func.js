function redirect(path) {
    window.location.replace(path)
}

function alertRedirect(msg,path) {
    alert(msg);
    window.location.replace(path)
}

function checkEmpty() {
    var length = arguments.length;
    for (var i = 0; i < length; i++) {
        var elemId = arguments[i];
        var elem = document.getElementById(elemId);
        if(elem.value === "") {
            alert('标题和内容不能为空')
            return false;
        }
    }
    return true
}

function getValue(name) {
    return document.getElementById(name).value;
}

function queryArticle(name) {
    var q = getValue(name)
    window.location.replace('/manage?q=' + q)
}

function selectDate() {
    var x = document.getElementById("date_picker").value;
    window.location.replace('?date='+x)
}

function reload() {
    window.reload()
}

// window.location.replace("/asd")