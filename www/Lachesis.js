var exec = require("cordova/exec");
var LachesisScanner = function () {};
LachesisScanner.prototype.start = function (success, error) {
  exec(success, error, "Lachesis", "start", []);
};
LachesisScanner.prototype.stop = function (success, error) {
  exec(success, error, "Lachesis", "stop", []);
};

module.exports = new LachesisScanner();
