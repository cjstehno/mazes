
$('button[type=submit]').click(_.bind(generateClicked, this));

function generateClicked(evt){
    evt.preventDefault();

    var mazeRequest = {};
    mazeRequest.rows = parseInt($('#rows').val());
    mazeRequest.cols = parseInt($('#cols').val());
    mazeRequest.passageWidth = parseInt($('#passageWidth').val());
    mazeRequest.braiding = parseInt($('#braiding').val());
    mazeRequest.algorithm = $('#algorithm').val();
    mazeRequest.visualization = $('#visualization').val();

    $.ajax({
        url: 'maze',
        type: 'POST',
        data: JSON.stringify(mazeRequest),
        contentType: 'application/json',
        success: _.bind(updateMazeImage, this)
    });
}

function updateMazeImage(){
    var imgElt = $('#maze-image');

    var srcUrl = imgElt.attr('src');
    if( srcUrl.indexOf('?') > 0 ){
        srcUrl = srcUrl.substring(0, srcUrl.indexOf('?'));
    }

    imgElt.attr('src', srcUrl + '?' + new Date().getTime());
}
