CKEDITOR.plugins.add( 'apage',
{
	var cmd = {
		exec:function(editor){
			editor.insertHtml("[[page]]");
		}
	}
	init : function( editor )
	{
		// Add the link and unlink buttons.
		editor.addCommand( 'apage', cmd );
		editor.ui.addButton( 'Page',
			{
				//label : editor.lang.link.toolbar,
				label : "Page",
				//icon: 'images/anchor.gif',
				command : 'apage'
			} );		
	},
 
	requires : [ 'fakeobjects' ]
} );