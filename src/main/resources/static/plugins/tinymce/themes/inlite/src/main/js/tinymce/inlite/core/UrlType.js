/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

define('tinymce/inlite/core/UrlType', [
], function () {
	var isDomainLike = function (href) {
		return /^www\.|\.(com|org|edu|gov|uk|net|ca|de|jp|fr|au|us|ru|ch|it|nl|se|no|es|mil)$/i.test(href.trim());
	};

	var isAbsolute = function (href) {
		return /^https?:\/\//.test(href.trim());
	};

	return {
		isDomainLike: isDomainLike,
		isAbsolute: isAbsolute
	};
});


