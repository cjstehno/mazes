(function ($) {

    $('button[type=submit]').click(function (evt) {
        evt.preventDefault();

        var mazeRequest = {};
        mazeRequest.rows = parseInt($('#rows').val());
        mazeRequest.cols = parseInt($('#cols').val());
        mazeRequest.braiding = parseInt($('#braiding').val());
        mazeRequest.algorithm = $('#algorithm').val();
        mazeRequest.visualization = $('#visualization').val();

        $.ajax({
            url: 'maze',
            type: 'POST',
            data: JSON.stringify(mazeRequest),
            contentType: 'application/json',
            success: function (resp, status) {
                console.log(status);
            }
        });
    });

}(jQuery));