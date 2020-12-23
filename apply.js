var PLUGIN_NAME = "CordovaEpochSDK";
var BUILD_GRADLE_PATH = "./platforms/android/app/build.gradle";

var deferral, fs, path;

function log(message) {
    console.log(PLUGIN_NAME + ": " + message);
}

function onFatalException(ex){
    log("EXCEPTION: " + ex.toString());
    deferral.resolve(); // resolve instead of reject so build doesn't fail
}

function run() {
  try {
      fs = require('fs');
      // path = require('path');
  } catch (e) {
      throw("Failed to load dependencies: " + e.toString());
  }
  let need_write = false;
  var buildGradle = fs.readFileSync(BUILD_GRADLE_PATH).toString();
  log("search buildGradle.");
  let search_str = "dirs 'libs'";
  let str = "repositories {\n    flatDir {\n        dirs 'libs'\n    }\n}"
  if (buildGradle.indexOf(search_str) == -1){
    //没有这个字符串
    log("try add epoch dirs to build gradle.");
    buildGradle += "\n" + str + "\n";//末尾添加
    need_write = true;
  }

  //添加arr的解析
  let search_str2 = "implementation(name: 'epoch_datasync_v1.1.1-androidX', ext: 'aar')";
  if (buildGradle.indexOf(search_str2) == -1){
    //没有这个字符串
    log("try add epoch implementation to build gradle.");
    let pos = buildGradle.indexOf("// SUB-PROJECT DEPENDENCIES END");
    log("pos="+pos);
    if (pos >= 0){
      buildGradle = buildGradle.replace("// SUB-PROJECT DEPENDENCIES END", "// SUB-PROJECT DEPENDENCIES END\n    implementation(name: 'epoch_datasync_v1.1.1-androidX', ext: 'aar')");
    }
    need_write = true;
  }

  if (need_write){
    fs.writeFileSync(BUILD_GRADLE_PATH, buildGradle, 'utf8');
  }
  ////运行结束
  deferral.resolve();
}

function attempt(fn) {
    return function () {
        try {
            fn.apply(this, arguments);
        } catch (e) {
            onFatalException(e);
        }
    }
}

module.exports = function (ctx) {
    console.log("CordovaEpochSDK ---------------------");
    try{
        deferral = require('q').defer();
    }catch(e){
        e.message = 'Unable to load node module dependency \'q\': '+e.message;
        onFatalException(e);
        throw e;
    }
    attempt(run)();
    return deferral.promise;
};