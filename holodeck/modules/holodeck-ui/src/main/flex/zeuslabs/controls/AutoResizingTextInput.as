////////////////////////////////////////////////////////////////////////////////
//
//  Copyright (c) 2006 Josh Tynjala
//
//  Permission is hereby granted, free of charge, to any person obtaining a copy
//  of this software and associated documentation files (the "Software"), to 
//  deal in the Software without restriction, including without limitation the
//  rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
//  sell copies of the Software, and to permit persons to whom the Software is
//  furnished to do so, subject to the following conditions:
//
//  The above copyright notice and this permission notice shall be included in
//  all copies or substantial portions of the Software.
//
//  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
//  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
//  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
//  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
//  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//  FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
//  IN THE SOFTWARE.
//
////////////////////////////////////////////////////////////////////////////////

package zeuslabs.controls
{
	import flash.events.TextEvent;
	import flash.events.KeyboardEvent;
	import mx.controls.*;

	/**
	 *  A TextInput control that will resizes immediately when text is changed,
	 *  unless an explicit size is set.
	 */
	public class AutoResizingTextInput extends TextInput
	{
		
	//--------------------------------------
	//  Constructor
	//--------------------------------------
	
		/**
		 *  Constructor.
		 */
		public function AutoResizingTextInput()
		{
			super();
			this.addEventListener(TextEvent.TEXT_INPUT, textInputHandler);
		}
	
	//--------------------------------------
	//  Protected Methods
	//--------------------------------------	
	
		/**
		 *  Whenever text is changed, we need to resize the component.
		 */
		protected function textInputHandler(event:TextEvent):void
		{
			this.invalidateSize();
		}
		
		/**
		 *  The textInputHandler isn't enough. We need to capture stuff like backspace too.
		 */
		override protected function keyDownHandler(event:KeyboardEvent):void
		{
			super.keyDownHandler(event);
			this.invalidateSize();
		}
		
		/**
		 *  Since the textfield resizes automatically, it shouldn't scroll.
		 *  However, a parent component could explicitly size it, so if the
		 *  final width is larger than the measured width, don't change it.
		 */
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			super.updateDisplayList(unscaledWidth, unscaledHeight);
			if(unscaledWidth <= this.measuredWidth) this.textField.scrollH = 0;
		}
	}
}