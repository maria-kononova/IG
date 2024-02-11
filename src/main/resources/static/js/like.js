function like(postId){
    var caller = event.target;
    const i  = caller.getElementsByTagName("i");
    var url = "/like";
    var n = 1;
    var classname = "fa-solid fa-heart";
    if(i[0].className==="fa-solid fa-heart"){
        url = "/unlike";
        n = -1;
        classname = "fa-regular fa-heart fa-bounce";
    }
    const formData = {
        postId: postId,
    };
    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        success: function (response) {
            if (response === "Success") {
                caller.getElementsByClassName('label').item(0).textContent = Number(caller.getElementsByClassName('label').item(0).textContent) + n;
                i[0].className = classname;
            }
            else {
                let myToast = Toastify({
                    text: "Вы не зарегистрированы!",
                    duration: 5000
                }).showToast()
            }
        }
    });
}

function matchLike(postId){
    const div = document.getElementsByClassName("div_post_item_list");
    for (let i = 0; i < div.length; i++) {
        if (Number(div[i].getElementsByTagName('p')[0].textContent) === postId) {
            selectedI = div[i].getElementsByTagName("i")[0];
            selectedI.className = "fa-solid fa-heart";
        }
    }
}

/*function like(postId) {
    const div = document.getElementsByClassName("div_post_item_list");
    let selectedI;
    let selectedLabel;
    for (let i = 0; i < div.length; i++) {
        if (div[i].getElementsByTagName('p')[0].textContent === postId) {
            selectedI =  div[i].getElementsByTagName("i").item(0);
            selectedLabel = div[i].getElementsByClassName('label').item(0);
        }
    }
    var caller = event.target;
    var url = "/like";
    var n = 1;
    var classname = "fa-solid fa-heart";
    if (selectedI.className === "fa-solid fa-heart") {
        url = "/unlike";
        n = -1;
        classname = "fa-regular fa-heart fa-bounce";
    }
    const formData = {
        postId: postId,
    };
    $.ajax({
        url: url,
        type: "POST",
        data: formData,
        success: function (response) {
            if (response === "Success") {
                selectedLabel.textContent = Number(selectedLabel.textContent) + n;
                selectedI.className = classname;
            } else {
                let myToast = Toastify({
                    text: "Вы не зарегистрированы!",
                    duration: 5000
                }).showToast()
            }
        }
    });
}

function matchLike(postId) {
    const div = document.getElementsByClassName("div_post_item_list");
    let selectedI;
    for (let i = 0; i < div.length; i++) {
        if (div[i].getElementsByTagName('p')[0].textContent === postId) {
            alert(postId);
            selectedI = div[i].getElementsByTagName("i")[0];
            selectedI.className = "fa-solid fa-heart";
        }
    }
}*/
