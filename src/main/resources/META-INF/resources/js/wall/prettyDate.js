



(function ($) {
    $.fn.prettyDate = function (options) {
        var all = this;

        if (typeof options == "undefined") {
            options = {};
        }
        if (typeof options.refreshInterval == "undefined") {
            options.refreshInterval = 60;
        }
        if (options.locale === undefined || options.locale === null || options.locale.length === 0) {
            options.locale = "no";
        }

        var clientTime = new Date().getTime();
        var serverTime = clientTime;
        var serverDate = parseDate(options.serverTime);
        if (serverDate != null) {
            serverTime = serverDate.getTime();
        }

        function portability(dateValue) {
            var day, tz,
                rx=/^(\d{4}\-\d\d\-\d\d([tT ][\d:\.]*)?)([zZ]|([+\-])(\d\d):(\d\d))?$/,
                p= rx.exec(dateValue) || [];
            if(p[1]){
                day= p[1].split(/\D/);
                for(var i= 0, L= day.length; i<L; i++){
                    day[i]= parseInt(day[i], 10) || 0;
                }
                day[1]-= 1;
                day= new Date(Date.UTC.apply(Date, day));
                if(!day.getDate()) return NaN;
                if(p[5]){
                    tz= (parseInt(p[5], 10)*60);
                    if(p[6]) tz+= parseInt(p[6], 10);
                    if(p[4]== '+') tz*= -1;
                    if(tz) day.setUTCMinutes(day.getUTCMinutes()+ tz);
                }
                return day;
            }
            return NaN;
        }
        function isValidDate(d) {
            if ( Object.prototype.toString.call(d) !== "[object Date]" )
                return false;
            return !isNaN(d.getTime());
        }
        function parseDate(dateValue) {
            var D = null;
            try {
                D = new Date(dateValue);
                if (!isValidDate(D)) {
                    D = null;
                }
            } catch (exception) {}
            if (!D) {
                try {
                    D = portability(dateValue);
                } catch (exception) {}
            }
            if (D) {
                return D;
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