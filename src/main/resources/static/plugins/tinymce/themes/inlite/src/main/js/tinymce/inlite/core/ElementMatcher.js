/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/ElementMatcher', [
	'tinymce/inlite/core/Matcher',
	'tinymce/inlite/core/Measure'
], function (Matcher, Measure) {
	// element :: Element, [PredicateId] -> (Editor -> Matcher.result | Null)
	var element = function (element, predicateIds) {
		return function (editor) {
			for (var i = 0; i < predicateIds.length; i++) {
				if (predicateIds[i].predicate(element)) {
					return Matcher.result(predicateIds[i].id, Measure.getElementRect(editor, element));
				}
			}

			return null;
		};
	};

	// parent :: [Elements], [PredicateId] -> (Editor -> Matcher.result | Null)
	var parent = function (elements, predicateIds) {
		return function (editor) {
			for (var i = 0; i < elements.length; i++) {
				for (var x = 0; x < predicateIds.length; x++) {
					if (predicateIds[x].predicate(elements[i])) {
						return Matcher.result(predicateIds[x].id, Measure.getElementRect(editor, elements[i]));
					}
				}
			}

			return null;
		};
	};

	return {
		element: element,
		parent: parent
	};
});
