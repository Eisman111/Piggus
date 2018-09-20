/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/alien/Arr', [
], function () {
	var flatten = function (arr) {
		return arr.reduce(function (results, item) {
			return Array.isArray(item) ? results.concat(flatten(item)) : results.concat(item);
		}, []);
	};

	return {
		flatten: flatten
	};
});
