/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/Matcher', [
], function () {
	// result :: String, Rect -> Matcher.result
	var result = function (id, rect) {
		return {
			id: id,
			rect: rect
		};
	};

	// match :: Editor, [(Editor -> Matcher.result | Null)] -> Matcher.result | Null
	var match = function (editor, matchers) {
		for (var i = 0; i < matchers.length; i++) {
			var f = matchers[i];
			var result = f(editor);

			if (result) {
				return result;
			}
		}

		return null;
	};

	return {
		match: match,
		result: result
	};
});
