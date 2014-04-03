



(function ($) {
    $.fn.prettyDate = function (options) {
        var all = this;

        if (typeof options == "undefined") {
            options = {};
        }
        if (typeof options.refreshInterval == "undefined") {
            options.refreshInterval = 60;
        }
        if (typeof options.locale == "undefined") {
            options.locale = "no";
        }

        var clientTime = new Date().getTime();
        var serverTime = clientTime;
        var serverDate = parseDate(options.serverTime);
        if (serverDate != null) {
            serverTime = serverDate.getTime();
        }

        function parseDate(dateValue) {
            var dateComp = dateValue.split("T");
            if (dateComp.length == 2) {
                var ymd = dateComp[0].split("-");
                var mhs = dateComp[1].split(":");
                if (ymd.length == 3 && mhs.length == 3) {
                    return new Date(ymd[0], ymd[1]-1, ymd[2], mhs[0], mhs[1], mhs[2]);
                }
            }
            return null;
        }

        function createPrettyDate(now, date) {
            var diffSeconds = (now.getTime() - date.getTime()) / 1000;
            var lastDayOfMonth = new Date(date.getFullYear(), date.getMonth(), 0).getDate();

            var locStrings = $.fn.prettyDate.regional[options.locale];

            var prettyDateTxt = null;
            if (diffSeconds < 60) {
                prettyDateTxt = locStrings.lessthanoneminutesince;
            } else if (diffSeconds < 60*2) {
                prettyDateTxt = locStrings.oneminutesince;
            } else if (diffSeconds < 60*60) {
                prettyDateTxt =  locStrings.minutessince.replace('%m', Math.floor(diffSeconds/60));
            } else if (diffSeconds < 60*60*2) {
                prettyDateTxt = locStrings.onehourago;
            } else if (diffSeconds < 60*60 * 24) {
                prettyDateTxt = locStrings.hoursago.replace('%h', Math.floor(diffSeconds/ (60 * 60)));
            } else if (diffSeconds < 60*60 * 2*24 && (now.getDate() - 1 == date.getDate()  || (now.getDate() == 1 && date.getDate() == lastDayOfMonth))) {
                prettyDateTxt = locStrings.yesterday.replace('%h', addLeadingZero(date.getHours())).replace('%m', addLeadingZero(date.getMinutes()));
            } else if (diffSeconds < 60*60 * 4*24) {
                prettyDateTxt = locStrings.weekdays[date.getDay()].replace('%h', addLeadingZero(date.getHours())).replace('%m', addLeadingZero(date.getMinutes()));
            }

            if (prettyDateTxt == null ) {
                prettyDateTxt = locStrings.months[date.getMonth()].replace('%d', date.getDate()).replace('%h', addLeadingZero(date.getHours())).replace('%m', addLeadingZero(date.getMinutes()));
            }
            return prettyDateTxt;
        }

        function addLeadingZero(d) {
            if (d < 10) {
                return "0" + d;
            } else {
                return d;
            }
        }

        function updatePrettyDate() {
            var now = new Date();
            now.setTime(serverTime + now.getTime() - clientTime);

            $(all).each(function(i, elm) {
                var $elm = $(all[i]);
                var dateValue = $elm.attr('date-data');
                $elm.html(createPrettyDate(now, parseDate(dateValue)));
            });

            setTimeout(updatePrettyDate, options.refreshInterval*1000);
        }

        updatePrettyDate();

        return this;
    };

    $.fn.prettyDate.regional = {};


})(jQuery);

(function ($) {
    $.fn.prettyDate.regional.no = {
        "lessthanoneminutesince" : "Mindre enn et minutt siden",
        "oneminutesince" : "1 minutt siden",
        "minutessince" : "%m minutter siden",
        "onehourago" : "1 time siden",
        "hoursago" : "%h timer siden",
        "yesterday" : "i g&aring;r kl %h:%m",
        "weekdays" : ["s&oslash;ndag kl %h:%m", "mandag kl %h:%m", "tirsdag kl %h:%m", "onsdag kl %h:%m", "torsdag kl %h:%m", "fredag kl %h:%m", "l&oslash;rdag kl %h:%m"],
        "months" : ["%d. januar kl %h:%m", "%d. februar kl %h:%m", "%d. mars kl %h:%m", "%d. april kl %h:%m", "%d. mai kl %h:%m", "%d. juni kl %h:%m", "%d. juli kl %h:%m", "%d. august kl %h:%m", "%d. september kl %h:%m", "%d. oktober kl %h:%m", "%d. november kl %h:%m", "%d. desember kl %h:%m"]
    };
    $.fn.prettyDate.regional.en = {
        "lessthanoneminutesince" : "Less than a minute ago",
        "oneminutesince" : "1 minute ago",
        "minutessince" : "%m minutes ago",
        "onehourago" : "1 hour ago",
        "hoursago" : "%h hours ago",
        "yesterday" : "Yesterday at %h:%m",
        "weekdays" : ["Sunday at %h:%m", "Monday at %h:%m", "Tuesday at %h:%m", "Wednesday at %h:%m", "Thursday at %h:%m", "Friday at %h:%m", "Saturday at %h:%m"],
        "months" : ["%d. January at %h:%m", "%d. February at %h:%m", "%d. March at %h:%m", "%d. April at %h:%m", "%d. May at %h:%m", "%d. June at %h:%m", "%d. July at %h:%m", "%d. August at %h:%m", "%d. September at %h:%m", "%d. October at %h:%m", "%d. November at %h:%m", "%d. December at %h:%m"]
    };
})(jQuery);