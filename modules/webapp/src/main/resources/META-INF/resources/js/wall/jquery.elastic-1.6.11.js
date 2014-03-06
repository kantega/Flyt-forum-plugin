/** 
 *  Another TextArea Autogrow plugin (0.2) alpha's alpha
 *  by Nikolay Borisov aka KOSIASIK
 *  mne@figovo.com
 *
 *  http://figovo.com/
 *
 *  Example: 
 *  $('textarea').ata();
 *
 *  jQuery required. Download it at http://jquery.com/
 *
 */


(function(jQuery){

	jQuery.fn.ata = function(options){

		options = jQuery.extend({
			timer:100
		}, options);
	
		return this.each(function(i){
	
			var $jQueryTextarea = jQuery(this),
				textarea = this;

			textarea.style.resize = 'none';
			textarea.style.overflow = 'hidden';

			var textareaVal = textarea.value;
			textarea.style.height = '0px';
			textarea.value = "W\nW\nW";
			var H3 = textarea.scrollHeight;
			textarea.value = "W\nW\nW";
			var H4 = textarea.scrollHeight;
			var H = H4 - H3;
			textarea.value = textareaVal;
			textareaVal = null;

			var $c = $('<div></div>');
            $jQueryTextarea.after($c);
		    var c = $c.get(0);

			c.style.padding = '0px';
			c.style.margin = '0px';

			//$jQueryTextarea.appendTo($c);

			$jQueryTextarea.bind('focus', function(){
				textarea.startUpdating()
			}).bind('blur', function(){
				textarea.stopUpdating()
			});

			this.heightUpdate = function(){

				if (textareaVal != textarea.value){

					textareaVal = textarea.value;
					textarea.style.height = '0px';
					var tH = textarea.scrollHeight + H;
                    if (tH <= 25) {
                        tH = 14;
                    }
					textarea.style.height = tH + 'px';
					c.style.height = 'auto';
					c.style.height = c.offsetHeight + 'px';

				}

			}

			this.startUpdating = function(){
				textarea.interval = window.setInterval(function(){
					textarea.heightUpdate()
				}, options.timer);
			}

			this.stopUpdating = function(){
				clearInterval(textarea.interval);
			}

			jQuery(function(){
				textarea.heightUpdate()
			});

		});

	};

})(jQuery);