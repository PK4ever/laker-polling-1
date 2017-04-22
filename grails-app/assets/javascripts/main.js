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
}