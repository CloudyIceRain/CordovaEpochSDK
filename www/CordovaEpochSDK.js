var exec = require('cordova/exec');

let CordovaEpochSDK= {
  initEpochSDK(arg0, arg1, success, error){
    exec(success, error, 'CordovaEpochSDK', 'initEpochSDK', [arg0, arg1]);
  },
  syncData(arg0,arg1,arg2,arg3, success, error){
    exec(success, error, 'CordovaEpochSDK', 'syncData', [arg0, arg1, arg2, arg3]);
  },
}
module.exports = CordovaEpochSDK;