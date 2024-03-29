function index_button() {
    let target = $('#collapseExample')
    if (target.hasClass('show')) {
        target.removeClass('show')
        $('#index_button').text('목차 펼치기')
    } else {
        target.addClass('show')
        $('#index_button').text('목차 숨기기')

    }
}

function ajaxAddCart(itemId, authenticated) {
    if (!authenticated) {
        window.location.href = '/member/login'
        return
    }
    let requestQuantity = 1

    if ($('#requestQuantity').val() > 0) {
        requestQuantity = $('#requestQuantity').val()
    }

    let jsonData = {
        "itemId": itemId,
        "requestQuantity": requestQuantity
    };

    $.ajax({
        type: "POST",
        url: "/cart",
        contentType: 'application/json',
        dataType: "text",
        data: JSON.stringify(jsonData),
        success: function (result) {
            if (result == 1) {
                $("#modal_text").text('선택하신 상품이 장바구니에 담겼습니다.장바구니로 이동하시겠습니까?')
                let count_tag = $("#shopping_cart_count")
                let count = parseInt(count_tag.text()) + 1
                count_tag.text(count)
            } else if (result == 2) {
                $("#modal_text").text('이미 장바구니에 담긴 상품이기때문에 수량이 추가되었습니다. 장바구니로 이동하시겠습니까?')
            } else if (result == 3) {
                $("#modal_text").text('재고가 충분하지 않습니다.')
            } else {
                window.location.href = '/member/login'
            }

        },
        error: function (e) {
            alert(e)
        }
    })
}

function ajaxPurchase(itemId, authenticated) {
    if (!authenticated) {
        window.location.href = '/member/login'
        return
    }
    let requestQuantity = 1

    if ($('#requestQuantity').val() > 0) {
        requestQuantity = $('#requestQuantity').val()
    }

    let jsonData = {
        "itemId": itemId,
        "requestQuantity": requestQuantity
    };

    $.ajax({
        type: "POST",
        url: "/cart",
        contentType: 'application/json',
        dataType: "text",
        data: JSON.stringify(jsonData),
        success: function (result) {
            if (result == 1 || result == 2) {
                window.location.href = '/order';
            } else if (result == 3) {
                alert("재고수량이 부족합니다.")
            } else {
                window.location.href = '/member/login'
            }
        },
        error: function (e) {
            alert(e)
        }
    })
}