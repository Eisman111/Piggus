/*
 * Piggus - Copyright (c) 2018 by Paolo Amosso
 * License: GNU Affero General Public License v3.0
 */

/**
 * Theme: Ubold Dashboard
 * Author: Coderthemes
 * Foo table
 */

jQuery(function($){
    $('#footable_1').footable();
    $('#footable_2').footable({
        "paging": {
            "enabled": true
        },
        "filtering": {
            "enabled": true
        }
    });
    $('#footable_3,#footable_4,#footable_5,#footable_6').footable({
        "paging": {
            "enabled": true
        },
        "sorting": {
            "enabled": true
        }
    });
});