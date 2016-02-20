function runMenu() {

    function getItem(item) {

        var tmpl = $(item.tmpl);

        if (item.sub) {

            var subTmpl = tmpl;

            $.each(item.sub, function () {

                var tag = getItem(this);

                if (subTmpl.children().last().length == 0) {
                    subTmpl.append(tag);

                } else {
                    subTmpl.children().last().append(tag);
                }
            });

        } else {

            if (tmpl.children().last().length == 0) {
                tmpl.append(item.name);

            } else {
                tmpl.children().last().append(item.name);
            }
        }

        return tmpl;
    }

    var pathname = window.location.pathname;

    var pathRequest = "/menu/json?item=" + pathname;

    $.getJSON(pathRequest, function (data) {

        var tmpl = $(data.tmpl);
        var items = data.items;

        $.each(items, function () {
            var tag = getItem(this);
            tmpl.append(tag);
        });

        $('#' + data.id).html(tmpl[0].outerHTML);

        eval(data.activeMarkerFunc);

        activeMarker(data.current); // the function body inside json data.
    });

    /*function activeMarker(target) {

        // Only support two level menu (/item/item.html) and the path has to start from '/' symbol.

        if (target.indexOf('/') !== -1) {

            var arr = target.split('/');

            if (arr.length === 3) {
                var firstItem = arr[1];
                $('a[href=\"' + firstItem + '\"]').parent().addClass("active");

            } else if (arr.length != 2) {
                console.error("Only two level menu supported.");
            }
        }
        $('a[href=\"' + target + '\"]').parent().addClass("active");
    }*/
}