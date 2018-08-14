
var express = require("express");
var bodyParser = require("body-parser");
var app = express();
var mysql = require('mysql');

app.use(bodyParser.json())



var server = app.listen(process.env.PORT || 8080, function () {
    var port = server.address().port;
    console.log("App now running on port", port);

});
``

var conn = mysql.createConnection({
    user:  "root",
    password: "",
    server: "127.0.0.1",
    database:"Contact"
});
conn.connect(function (err) {
    if (err) throw err;
    console.log("Connected!");
});


var bulk_update = function(req,res) {

    var sql = "INSERT INTO contactdetail (name, contact) VALUES ?";
    var values = [];
    var json_body = req.body
    var list = []
       list.push(json_body.name)
       list.push(json_body.contact)
       values.push(list)
    conn.query(sql, [values], function(err) {
        if (err) throw err;
        var obj = {
            Message:"Data Loaded"
        }
        res.send(obj)
    });
}


//GET API
app.get("/api/contacts/get", function(req , res){
    var query = "SELECT * FROM contactdetail;";
    conn.query(query, function (error, results) {
        if (error) throw error;
        res.send(results)
    });
});

//POST API
app.post("/api/contacts/post", function(req , res){
    bulk_update(req,res)
});
