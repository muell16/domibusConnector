package sho.ui 
{
    import flash.events.Event;
    import flash.events.MouseEvent;
    import flash.geom.Rectangle;
    import flash.geom.Point;

    import mx.containers.Panel;
    import mx.controls.Button;
    import mx.core.Container;
    import mx.effects.Effect;
    import mx.effects.Move;
    import mx.events.EffectEvent;
    import mx.events.MoveEvent;
    import mx.events.ResizeEvent;
    import mx.managers.PopUpManager;
    import mx.events.FlexEvent;
    import flash.utils.Timer;
    import flash.events.TimerEvent;

    public class Drawer extends Panel 
    {
        // -------- Constants --------
        
        public static const LEFT    : String = "left";
        public static const RIGHT    : String = "right";
        public static const TOP        : String = "top";
        public static const BOTTOM    : String = "bottom";
                
        // -------- Public properties --------
        
        // dockSide: String (read/write)
        //
        // Specifies the side of the parent container that the drawer
        // will pull out from. Must be one of: LEFT, RIGHT, TOP, 
        // or BOTTOM
        [Inspectable]
        public var dockSide : String = LEFT;
        
        // initiallyShowing: Boolean (read/write)
        //
        // By default, drawers start out visible, and then animate back
        // into a hidden position. This is done so that users know that
        // the drawers are there. If you would like to turn this behavior
        // off, set initiallyShowing to false.
        [Inspectable]
        public var initiallyShowing : Boolean = true;
        
        // -------- Constructor --------
        
        public function Drawer()
        {
            super();
            
            // TODO: Make sure to define these defaults in a way
            // that it can be overridden.
            setStyle("cornerRadius", 0);
            setStyle("borderThicknessTop", 0);
            setStyle("borderThicknessLeft", 0);
            setStyle("borderThicknessBottom", 0);
            setStyle("borderThicknessRight", 0);
            setStyle("backgroundAlpha", 0.75);
            
            this.addEventListener(FlexEvent.CREATION_COMPLETE, _onCreationComplete);
        }
        
        // -------- Private event handlers --------
                
        private function _onCreationComplete(event: Event) : void
        {
            this.removeEventListener(FlexEvent.CREATION_COMPLETE, _onCreationComplete);
            
            realParent = parent as Container;
            
            // Remove from list of normal children.
            realParent.removeChild(this);

            // TODO: fix this weird workaround - the intialization
            // code is getting confused by how I am reparenting during
            // intialization.
            this.initialized = true;
            
            // Add it as a popup.
            PopUpManager.addPopUp(this, realParent);
                        
            // Add listeners.
            realParent.addEventListener(MouseEvent.MOUSE_MOVE, _onMouseMove);
            realParent.addEventListener(ResizeEvent.RESIZE, onParentResize);
            realParent.addEventListener(FlexEvent.SHOW, onParentVisibilityChange);
            realParent.addEventListener(FlexEvent.HIDE, onParentVisibilityChange);
            this.stage.addEventListener(Event.MOUSE_LEAVE, _onMouseLeave);

            this.addEventListener(MouseEvent.MOUSE_MOVE, _onMouseMove);

            this.addEventListener(MoveEvent.MOVE, onMove);
            this.addEventListener(ResizeEvent.RESIZE, onResize);        

            // Add thumbtack button.
            thumbTackButton = new Button();
            thumbTackButton.width = TACK_BUTTON_WIDTH;
            thumbTackButton.height = TACK_BUTTON_HEIGHT;
            thumbTackButton.setStyle("cornerRadius", 0);
            thumbTackButton.setStyle("icon", circleIcon);
            this.rawChildren.addChild(thumbTackButton);
                
            thumbTackButton.addEventListener(MouseEvent.CLICK, onThumbTackClick);
            
            // Save the original height and width -- this is used when the 
            // panel is floating.
            originalHeight = height;
            originalWidth = width;
            
            // Initialize myself by calling the listeners explicitly that
            // need to be called.
            this.visible = false;
            onParentVisibilityChange(null);
            onParentResize(null);
        }
        
        private function _onMouseMove(event: MouseEvent) : void
        {
            // If the state is floating or tacked, we don&apos;t have
            // to do anything on mouse move.
            if (tackState == FLOATING || tackState == TACKED)
                return;
            
            if (!visible)
                return;
            
            if (mouseLeaveTimer != null)
            {
                // We detected a mouse movement inside the movie. We
                // should halt the "mouse left the movie" timer.
                mouseLeaveTimer.stop();
                mouseLeaveTimer = null;
            }
            
            // Get the mouse position in local coordinates relative to parent.
            var point : Point = new Point(this.mouseX, this.mouseY);
            point = this.localToGlobal(point);
            point = realParent.globalToLocal(point);
            
            // Get the hit detect region for the drawer to appear.
            var rect : Rectangle;

            switch(dockSide)
            {
            case LEFT:
                rect = new Rectangle(0, 0, HIT_AREA_WIDTH, realParent.height);
                break;
            case TOP:
                rect = new Rectangle(0, 0, realParent.width, HIT_AREA_WIDTH);
                break;
            case RIGHT:
                rect = new Rectangle(realParent.width-HIT_AREA_WIDTH, 0, HIT_AREA_WIDTH, realParent.height);
                break;
            case BOTTOM:
                rect = new Rectangle(0, realParent.height-HIT_AREA_WIDTH, realParent.width, HIT_AREA_WIDTH);
                break;
            }

            if (tackState == SHOWING)
            {
                // Get the rectangle of the drawer itself.
                var boundsRect : Rectangle = new Rectangle(x, y, width, height);
                boundsRect.topLeft = realParent.globalToLocal(boundsRect.topLeft);
                boundsRect.bottomRight = realParent.globalToLocal(boundsRect.bottomRight);
                
                rect = rect.union(boundsRect);
            }
            
            // See if the mouse is over the rectangle. If so, pull the
            // drawer out. If not, hide the drawer.
            var shouldShow : String = rect.contains(point.x, point.y) ? SHOWING : HIDDEN;
            
            if (shouldShow != tackState)
            {
                animate(shouldShow == SHOWING);
                tackState = shouldShow;
            }
            
            doneInitialShow = true;
        }

        private function _onMouseLeave(event: Event) : void
        {
            if (tackState == SHOWING)
            {
                // If the mouse leaves the bounds of the Flash movie,
                // we want to close the drawer. However, I found it
                // was too finicky to have the drawer close immediately,
                // so we wait a second to close the drawer.
                mouseLeaveTimer = new Timer(1000, 1);
                mouseLeaveTimer.start();
                mouseLeaveTimer.addEventListener(TimerEvent.TIMER, onMouseLeaveTimer);
            }
        }
        
        private function onMouseLeaveTimer(event: Event) : void
        {
            // Stop the mouseLeaveTimer.
            mouseLeaveTimer.removeEventListener(TimerEvent.TIMER, onMouseLeaveTimer);
            mouseLeaveTimer.stop();
            mouseLeaveTimer = null;
            
            // Animate the drawer closed.
            animate(false);
            tackState = HIDDEN;
        }
        
        private function onParentResize(event: ResizeEvent) : void
        {
            if (tackState == FLOATING)
            {
                fitToBounds();
                return;
            }
                
            // Resize the drawer to be the same size as the parent.
            if (dockSide == LEFT || dockSide == RIGHT)
            {
                if (height != realParent.height)
                    height = realParent.height;
            }
            else
            {
                if (width != realParent.width)
                    width = realParent.width;
            }
            
            // Get the new position and set it.
            var shouldShow : Boolean;
            if (initiallyShowing && !doneInitialShow)
                shouldShow = true;
            else
                shouldShow = (tackState == SHOWING || tackState == TACKED);
                
            var point : Point = getPosition(shouldShow);
            setPosition(point);
        }
        
        private function onResize(event: ResizeEvent) : void
        {
            thumbTackButton.x = this.width - TACK_BUTTON_WIDTH - 4;
            thumbTackButton.y = 4;
        }
        
        private function onParentVisibilityChange(event: Event) : void
        {
            if (this.visible != realParent.visible)
            {
                this.visible = realParent.visible;
            }
        }
        
        private function onThumbTackClick(event: Event) : void
        {
            if (tackState == FLOATING)
            {
                thumbTackButton.setStyle("icon", circleIcon);
                var e : Effect = animate(false);
                e.addEventListener(EffectEvent.EFFECT_END, function(event: Event) : void { tackState = HIDDEN; onParentResize(null) });
            }
            else if (tackState != TACKED)
            {
                tackState = TACKED;
                thumbTackButton.setStyle("icon", circleDotIcon);
            }
            else
            {
                tackState = SHOWING;
                thumbTackButton.setStyle("icon", circleIcon);
            }
        }
        
        private function onMove(event: MoveEvent) : void
        {
            if (tackState != FLOATING)
            {
                // HACK: We determine if the user has ripped the drawer
                // off the side by seeing if the x/y coordinate looks
                // like a precomputed value.
                var zeroPoint : Point = new Point(0, 0);
                var thisPoint : Point = localToGlobal(zeroPoint);
                var parentPoint : Point = realParent.localToGlobal(zeroPoint);
                var isRipped : Boolean = false;
                
                if (dockSide == LEFT || dockSide == RIGHT)
                {
                    if (thisPoint.y != parentPoint.y)
                        isRipped = true;
                }
                else
                {
                    if (thisPoint.x != parentPoint.x)
                        isRipped = true;
                }
                
                if (isRipped)
                {
                    tackState = FLOATING;
                    thumbTackButton.setStyle("icon", xIcon);
                    width = originalWidth;
                    height = originalHeight;
                }
            }
        }

        // -------- Private methods --------
        
        private function animate(shouldShow: Boolean) : Effect
        {
            var moveTo : Point;
            
            if (moveEffect != null)
            {
                moveEffect.pause();
                moveEffect = null;
            }
                
            moveEffect = new Move(this);

            moveTo = getPosition(shouldShow);

            moveEffect.xTo = moveTo.x;
            moveEffect.yTo = moveTo.y;

            moveEffect.play();
                
            return moveEffect;
        }

        private function getPosition(visible : Boolean) : Point
        {
            var point : Point;
            
            switch(dockSide)
            {
            case LEFT:
                if (visible)
                    point = new Point(0, 0);
                else
                    point = new Point(-width, 0);
                break;
            case TOP:
                if (visible)
                    point = new Point(0, 0);
                else
                    point = new Point(0, -height);
                break;
            case RIGHT:
                if (visible)
                    point = new Point(realParent.width - width, 0);
                else
                    point = new Point(realParent.width, 0);
                break;
            case BOTTOM:
                if (visible)
                    point = new Point(0, realParent.height - height);
                else
                    point = new Point(0, realParent.height);
                break;
            }
            
            point = realParent.localToGlobal(point);
            return point;
        }
        
        private function setPosition(point: Point) : void
        {
            x = point.x;
            y = point.y;
        }

        private function fitToBounds() : void
        {
            // Get the drawer’s max used coordinates
            var actualXEnd : Number = this.x + this.width;
            var actualYEnd : Number = this.y + this.height;
        
            // Get the actual position coordinates
            var newPoint : Point = new Point(this.x, this.y);
        
            // Modify the coordinates only when needed
            if (realParent.width < actualXEnd)
                newPoint.x = Math.max(0, realParent.width - this.width - 10);
            
            if (realParent.height < actualYEnd)
                newPoint.y = Math.max(0, realParent.height - this.height - 10);
        
            // Adjust the position
            setPosition(newPoint);
        }
    
        // -------- Private variables --------
        
        private var realParent : Container;
        private var isShowing : Boolean;
        private var moveEffect : Move;
        private var originalHeight : Number;
        private var originalWidth : Number;
        private var tackState : String;
        private var thumbTackButton : Button;
        private var doneInitialShow : Boolean;
        
        private var mouseLeaveTimer : Timer;
        
        [Embed(source="circle.png")]
        [Bindable]
        private var circleIcon:Class;
        
        [Embed(source="circle_dot.png")]
        [Bindable]
        private var circleDotIcon:Class;
        
        [Embed(source="x.png")]
        [Bindable]
        private var xIcon:Class;
        
        private static const HIDDEN : String = "hidden";
        private static const SHOWING : String = "showing";
        private static const TACKED : String = "tacked";
        private static const FLOATING : String = "floating";

        private static const TACK_BUTTON_WIDTH :  Number = 20;
        private static const TACK_BUTTON_HEIGHT : Number = 20;
        private static const HIT_AREA_WIDTH : Number = 20;
    }
}