/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/PredicateId', [
	'global!tinymce.util.Tools'
], function (Tools) {
	var create = function (id, predicate) {
		return {
			id: id,
			predicate: predicate
		};
	};

	// fromContextToolbars :: [ContextToolbar] -> [PredicateId]
	var fromContextToolbars = function (toolbars) {
		return Tools.map(toolbars, function (toolbar) {
			return create(toolbar.id, toolbar.predicate);
		});
	};

	return {
		create: create,
		fromContextToolbars: fromContextToolbars
	};
});
