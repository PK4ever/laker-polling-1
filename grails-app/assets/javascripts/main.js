//define string replace all method
String.prototype.replaceAll = function(search, replacement) {
    var target = this;
    return target.replace(new RegExp(search, 'g'), replacement);
};

//a function for checking if an value is a function
const ObjectUtils = function() {}
const FuncUtils = function() {}
const ArrayUtils = function() {}
const NetworkUtils = function() {}

ObjectUtils.isObject = function (objToCheck) {
	return objToCheck  
	&& Object.prototype.toString.call(objToCheck) === '[object Object]';
    return objToCheck  
    && Object.prototype.toString.call(objToCheck) === '[object Object]';
}
ObjectUtils.isObj = ObjectUtils.isObject

FuncUtils.isFunc = function(functionToCheck) {
	var getType = {};
	return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

ArrayUtils.forEachCachedLength = function(array, callback) {
	if(!ArrayUtils.isArray(array)) return false;
	if(!FuncUtils.isFunc(callback)) throw new Error("forEach() requires callback function.")
	for (var i = 0, len = array.length; i < len; i++) {
		(function(i) {
			callback(array[i])
		}(i))
	}
}

ArrayUtils.isArray = function(objToCheck) {
	return objToCheck && Object.prototype.toString.call( objToCheck ) === '[object Array]';
}

ArrayUtils.isEmptyArray = function (arr) {
	if(!ArrayUtils.isArray(arr)) return true
	return arr.length < 1
}

NetworkUtils.runAjax = function(url, method, onSuccess, onError) {
	$.ajax({
		url: url,
		method: method,
		success: onSuccess,
		error: onError
	})
    var getType = {};
    return functionToCheck && getType.toString.call(functionToCheck) === '[object Function]';
}

ArrayUtils.forEachCachedLength = function(array, callback) {
    if(!ArrayUtils.isArray(array)) return false;
    if(!FuncUtils.isFunc(callback)) throw new Error("forEach() requires callback function.")
    for (var i = 0, len = array.length; i < len; i++) {
        (function(i) {
            callback(array[i])
        }(i))
    }
}

ArrayUtils.isArray = function(objToCheck) {
    return objToCheck && Object.prototype.toString.call( objToCheck ) === '[object Array]';
}

ArrayUtils.isEmptyArray = function (arr) {
    if(!ArrayUtils.isArray(arr)) return true
    return arr.length < 1
}

NetworkUtils.runAjax = function(url, method, onSuccess, onError) {
    $.ajax({
        url: url,
        method: method,
        success: onSuccess,
        error: onError
    })
}

NetworkUtils.getCurrentPathData = function() {
    const websiteLocation = window.location
    const websiteHostName = websiteLocation.host || websiteLocation.hostname
    const websitePathName = websiteLocation.pathname.trim()
    const pathArray = websitePathName.split( '/' )
    return {
        location: websiteHostName,
        host: websiteHostName,
        path: websitePathName,
        pathArray: pathArray || []
    }
}

NetworkUtils.getCurrentPathParamAtIndex = function(index) {
    let pathArray = NetworkUtils.getCurrentPathData().pathArray
    if(pathArray.length < index) return
    return pathArray[index]
}

NetworkUtils.getCurrentPathEquals = function(path) {
    return NetworkUtils.getCurrentPathData().path == path
}

NetworkUtils.getCurrentPathNthParamMatches = function(index, paramToCheck) {
    return NetworkUtils.getCurrentPathParamAtIndex(index) == paramToCheck
}

NetworkUtils.getCurrentLocationQueryParam = function(name) {
    var urlParamsString = window.location.search.replace('?', '')
    var params = urlParamsString.split("&")
    for (var i = 0; i < params.length; i++) {
        const paramParts = params[i].split('=')
        if (paramParts[0] == name) {
            return paramParts[1]
        }
    }
}