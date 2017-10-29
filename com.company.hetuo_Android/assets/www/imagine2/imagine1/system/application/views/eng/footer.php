<div id="footer"> 
	<span><a href="<?=site_url('globals/index/chi');?>">Home</a><b>|</b>
		<a href="<?=site_url('globals/prolist/chi');?>">Product</a><b> |</b>
		<a href="<?=site_url('globals/company/chi');?>">Company</a><b>|</b>
		<a href="<?=site_url('globals/news/chi');?>">News</a><b>|</b>
		<a href="<?=site_url('globals/contact/chi');?>">Contact Us</a><b>|</b>
	<br/>
    <span>Copyright @ HeTuo technology</span> </div>
<script src="system/js/jy.js"></script>
<script>
	var Jquery_func = {
			product_select: function(){ 
				window.location.href = $(this).attr('value');
			}
	}
	var normal_func = {
		product_select: function(){
			var get_value = '<?=$select?>';
				switch (get_value) {
					case 'index': {
						$("#fastSearch option[index='0']").attr('selected', true);
						break; 
					}
					case 'corporate': {
						$("#fastSearch option[index='1']").attr('selected', true);
						break; 
					}
					case 'tv': {
						$("#fastSearch option[index='2']").attr('selected', true);
						break; 
					}
					case 'print': {
						$("#fastSearch option[index='3']").attr('selected', true);
						break; 
					}
					case 'advertising': {
						$("#fastSearch option[index='4']").attr('selected', true);
						break; 
					}
					case 'promotion': {
						$("#fastSearch option[index='5']").attr('selected', true);
						break; 
					}
					case 'event': {
						$("#fastSearch option[index='6']").attr('selected', true);
						break; 
					}
					case 'premeium': {
						$("#fastSearch option[index='7']").attr('selected', true); 
						break; 
					}
					case 'video': {
						$("#fastSearch option[index='8']").attr('selected', true); 
						break; 
					}
					default: {
						$("#fastSearch option[index='0']").attr('selected', true);
						break;							
					}
				}
		}
	}
$("#fastSearch").change(Jquery_func.product_select);
normal_func.product_select();
</script>