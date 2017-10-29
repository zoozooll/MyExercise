<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<base href="<?=base_url()?>" />

<link type="text/css" rel="stylesheet" href="system/css/backend/general.css"/>
<link type="text/css" rel="stylesheet" href="system/css/backend/table.css"/>
<script src="system/js/mts.js"></script>
<script type="text/javascript" src="system/js/ckeditor.js"></script>
<script src="system/js/sample.js" type="text/javascript"></script>
<link href="system/css/sample.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript">
	//<![CDATA[

// The instanceReady event is fired when an instance of CKEditor has finished
// its initialization.
CKEDITOR.on( 'instanceReady', function( ev )
{
	// Show the editor name and description in the browser status bar.
	document.getElementById( 'eMessage' ).innerHTML = '<p>Instance "' + ev.editor.name + '" loaded.<\/p>';

	// Show this sample buttons.
	document.getElementById( 'eButtons' ).style.visibility = '';
});

function InsertHTML()
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;
	var value = document.getElementById( 'plainArea' ).value;

	// Check the active editing mode.
	if ( oEditor.mode == 'wysiwyg' )
	{
		// Insert the desired HTML.
		oEditor.insertHtml( value );
	}
	else
		alert( 'You must be on WYSIWYG mode!' );
}

function SetContents()
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;
	var value = document.getElementById( 'plainArea' ).value;

	// Set the editor contents (replace the actual one).
	oEditor.setData( value );
}

function GetContents()
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;

	// Get the editor contents
	alert( oEditor.getData() );
}

function ExecuteCommand(commandName)
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;

	// Check the active editing mode.
	if ( oEditor.mode == 'wysiwyg' )
	{
		// Execute the command.
		oEditor.execCommand( commandName );
	}
	else
		alert( 'You must be on WYSIWYG mode!' );
}

function CheckDirty()
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;
	alert( oEditor.checkDirty() );
}

function ResetDirty()
{
	// Get the editor instance that we want to interact with.
	var oEditor = CKEDITOR.instances.editor1;
	oEditor.resetDirty();
	alert( 'The "IsDirty" status has been reset' );
}

	//]]>
	</script>