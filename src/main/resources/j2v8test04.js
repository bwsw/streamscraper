
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
        // init
        id = makeid();
        kv_store_set(id,1);
        id = makeid();
        var v = kv_store_get(id);
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
    'commit_interval': 30,
    'terminate_on_bad_data': false,
    'terminate_on_bad_eval': false,

};
