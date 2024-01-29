let statePageIsAccount = false;
if ( document.URL.includes("account") ) {
    statePageIsAccount = true;
}
function getCookie(name) {
    name = name + "=";
    var cookies = document.cookie.split(';');
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i];
        while (cookie.charAt(0) == ' ') {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}
window.onload = function () {
    document.documentElement.style.setProperty('--main-color', getCookie("mainColor"));
    document.documentElement.style.setProperty('--second-color', getCookie("secondColor"));

    if (getCookie("theme") === "white") {
        document.documentElement.style.setProperty('--background-color', "#fff");
        if(statePageIsAccount){
            document.getElementById('toggle_checkbox').checked = false;
        }
    } else {
        document.documentElement.style.setProperty('--background-color', "#28292c");
        if(statePageIsAccount) {
            document.getElementById('toggle_checkbox').checked = true;
        }
    }

    if (getCookie("text") === "white"){
        document.documentElement.style.setProperty('--text-color', "#fff");
        if(statePageIsAccount){
            document.getElementById('toggle_checkbox_text').checked = false;
        }
    }
    else{
        document.documentElement.style.setProperty('--text-color', "#000");
        if(statePageIsAccount) {
            document.getElementById('toggle_checkbox_text').checked = true;
        }
    }
    if(statePageIsAccount){
        document.getElementById('primary_color').value = getCookie("mainColor");
        document.getElementById('second_color').value = getCookie("secondColor");
        document.getElementById('textColor').value = getCookie("mainColor");
        document.getElementById('textSecondColor').value = getCookie("secondColor");
    }
    document.getElementsByTagName("html")[0].style.visibility = "visible";
}