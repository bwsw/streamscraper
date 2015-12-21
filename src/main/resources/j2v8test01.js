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
        id = makeid();
        kv_store_set(id,1);
        var v = kv_store_get(id);
    },
    'shutdown': function() {
    },
    'commit': function() {
    },
    'process': function(data) {
    },
    'commit_interval': 60
};
