(function() {
    $(document).ready(function() {
        var body,nav;
        nav = $("#main-nav");
        body = $("body");
        
        $("#main-nav .dropdown-collapse").on("click", function(e) {
            var link, list;

            e.preventDefault();
            link = $(this);
            list = link.parent().find("> ul");
            if (list.is(":visible")) {
                if (body.hasClass("main-nav-closed") && link.parents("li").length === 1) {
                    false;
                } else {
                    link.removeClass("in");
                    list.slideUp(300, function() {
                        return $(this).removeClass("in");
                    });
                }
            } else {
                link.addClass("in");
                list.slideDown(300, function() {
                    return $(this).addClass("in");
                });
            }
            return false;
        });

    });

}).call(this);