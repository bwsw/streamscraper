
function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 5; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

var handler = {
    'init': function () {
    },
    'shutdown': function() {
        // on shutdown
    },
    'commit': function() {
        // on commit
    },
    'process': function(data) {
        // on process
    },
    'commit_interval': 60,
    'terminate_on_bad_data': true,
    'terminate_on_bad_eval': true,
};