package com.ruialmeida.lumbridge.widgets.tooltips

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.LayoutInflater
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.IntegerRes
import androidx.annotation.LayoutRes
import androidx.annotation.StyleRes
import com.ruialmeida.lumbridge.R
import com.ruialmeida.lumbridge.extensions.bindings.afterMeasured
import org.jetbrains.anko.windowManager

class PopupTooltip(
    private var anchorView: View,
    private var message: CharSequence,
    private var indefinite: Boolean = false,
    private var showDuration: Long = 3500L,
    private var arrow: Boolean = false,
    private var verticalMargin: Int = 0,
    private var horizontalMargin: Int = 0,
    private var horizontalMarginClipped: Int = 0,
    private var verticalMarginClipped: Int = 0,
    private var insideTouchDismissible: Boolean = true,
    private var outsideTouchDismissible: Boolean = true,
    private var modal: Boolean = true,
    private var respectScreenBounds: Boolean = false,
    @DimenRes private var touchDelegateTouchAreaIncreaseAmount: Int = R.dimen.dimens_widget_popup_tooltip_touch_area_increase_default,
    @DimenRes private var layoutWidthResource: Int = R.dimen.dimens_widget_popup_tooltip_width,
    @DimenRes private var elevation: Int = R.dimen.dimens_widget_popup_tooltip_default_elevation,
    @StyleRes private var animation: Int = R.style.styles_widget_popup_tooltip_window_animation,
    @LayoutRes private var layout: Int = R.layout.layout_widget_popup_tooltip,
    @DrawableRes private var arrowDrawable: Int = R.drawable.drawable_widget_popup_tooltip_arrow_down,
    @ColorRes private var arrowStrokeColor: Int = android.R.color.white
) : PopupWindow() {

    companion object {

        /**
         * The LOGGING TAG. Yes, it's a useless comment, move along.
         */
        const val TAG = "PopupTooltip"

        /**
         * Saves a reference to our currently displayed tooltip. This is here so we can control when
         * we dismiss/show it.
         */
        private var tooltip: PopupWindow? = null

        /**
         * The estimated height of our popup in dips (density-independent pixels).
         * This assumes the popup will have two lines of text with the text sizes
         * specified in [R.layout.tooltip_layout].
         */
        const val ESTIMATED_HEIGHT_DIPS = 46

        /**
         * Set the gravity to the top left of the screen. In the future the idea is for this
         * to be changeable.
         */
        const val GRAVITY = Gravity.NO_GRAVITY

        /**
         * Sets the tooltip as null after showDuration amount of time. This is only used
         * when we set the tooltip as definite.
         */
        private val handler = Handler()

        /**
         * Clears the current reference to the tooltip and dismisses if it is present.
         */
        fun clearTooltipIfNotNull() {
            tooltip?.apply {
                if (isShowing) dismiss()
                tooltip = null
            }
        }
    }

    /**
     * The vertical offset from the anchorview to the tooltip.
     */
    fun verticalMargin(margin: Int) = apply { this.verticalMargin = margin }

    /**
     * The horizontal offset from the edges of the screen.
     */
    fun horizontalMargin(margin: Int) = apply { this.horizontalMargin = margin }

    /**
     * The vertical offset from the anchorview to the tooltip when the bounds of the tooltip exceed
     * the bounds of the screen.
     */
    fun verticalMarginClipped(margin: Int) = apply { this.verticalMarginClipped = margin }

    /**
     * The horizontal offset from the edges of the screen to the tooltip when the bounds
     * of the tooltip exceed the bounds of the screen.
     */
    fun horizontalMarginClipped(margin: Int) = apply { this.horizontalMarginClipped = margin }

    /**
     * If true the tooltip will dismiss after a certain amount of time. Else, it will remain there
     * until clicked again.
     */
    fun indefinite(indefinite: Boolean) = apply { this.indefinite = indefinite }

    /**
     * Flag to draw the arrow at the bottom of the tooltip, at the top center of the anchor view.
     * In the future, the idea is to make this customizable e.g, draw the tooltip at the right, bottom, etc.
     */
    fun arrow(showArrow: Boolean) = apply { this.arrow = showArrow }

    /**
     * Changes the arrow drawable to a specified one.
     */
    fun arrowDrawable(@DrawableRes drawable: Int) = apply { this.arrowDrawable = drawable }

    /**
     * Changes the arrow drawable stroke color.
     */
    fun arrowStrokeColor(@ColorRes strokeColor: Int) = apply { this.arrowStrokeColor = strokeColor }

    /**
     * If the tooltip is definite, show the tooltip for a certain amount of seconds.
     */
    fun timeToDismiss(timeInSeconds: Long) = apply { this.showDuration = timeInSeconds }

    /**
     * Allows to set custom animations to show.
     */
    fun animationStyle(showAnimation: Int) = apply { this.animation = showAnimation }

    /**
     * Enables or disables dismiss on outside touch.
     */
    fun outsideTouchDismissable(isDismissibleOnOutsideTouch: Boolean) = apply { this.outsideTouchDismissible = isDismissibleOnOutsideTouch }

    /**
     * Enables or disables dismiss on outside touch.
     */
    fun insideTouchDismissible(isOutsideTouchDismissible: Boolean) = apply { this.insideTouchDismissible = isOutsideTouchDismissible }

    /**
     * Defines the popup window as focusable. Set this to false if you want the view
     * to be click through. Although this messes up with the inside/outside touch flags.
     */
    fun modal(isModal: Boolean) = apply { this.modal = isModal }

    /**
     * Sets clip to children flag.
     */
    @Deprecated("This is sort of useless, since the widget handles this own its own. However, if it helps you in your use case, the option is here.")
    fun respectScreenBounds(clipToChildren: Boolean) = apply { this.respectScreenBounds = clipToChildren }

    /**
     * The tooltip width to be displayed. By default this is defined in the dimens.xml tooltip_popup_width.
     */
    fun layoutWidth(@DimenRes width: Int) = apply { this.layoutWidthResource = width }

    /**
     * Defines a custom layout for this tooltip.
     */
    fun layout(@LayoutRes layout: Int) = apply { this.layout = layout }

    /**
     * Defines a custom elevation for all the tooltip components.
     */
    fun elevation(@DimenRes elevation: Int) = apply { this.elevation = elevation }

    /**
     * Creates a touch delegate that increases the bounds of the view's touch area by a certain amount.
     */
    fun useTouchDelegateToIncreaseViewTouchArea(increaseTouchArea: Boolean) = apply { setupTouchDelegate(anchorView, anchorView.context) }

    /**
     * Defines the touch area's view increase.
     */
    fun touchDelegateTouchAreaIncreaseAmount(@IntegerRes increaseAmount: Int) = apply { this.touchDelegateTouchAreaIncreaseAmount = increaseAmount }

    /**
     * Displays the tooltip
     */
    fun show() = setupTooltip(anchorView, message)

    /**
     * Internal helper class to show the tooltip.
     */
    @SuppressLint("InflateParams")
    private fun setupTooltip(view: View, text: CharSequence?) {
        handler.removeCallbacksAndMessages(null)

        if (tooltip != null) {
            clearTooltipIfNotNull()
            return
        }

        if (TextUtils.isEmpty(text)) {
            return
        }

        val screenPos = IntArray(2)
        val displayFrame = Rect()
        val context = view.context

        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val tooltipWidth = context.resources.getDimension(layoutWidthResource).toInt()
        val tooltipArrowWidth = context.resources.getDimension(R.dimen.dimens_widget_popup_tooltip_arrow_width)

        view.getLocationOnScreen(screenPos)
        view.getWindowVisibleDisplayFrame(displayFrame)

        val estimatedTooltipHeight = (ESTIMATED_HEIGHT_DIPS * context.resources.displayMetrics.density).toInt()

        //Custom tooltip layout
        val layout = inflater.inflate(layout, null)

        //Custom text
        val textView = layout.findViewById<TextView>(R.id.text)
        textView.text = text
        textView.elevation = context.resources.getDimension(elevation)

        //Background
        val backgroundView = layout.findViewById<View>(R.id.layout_widget_popup_tooltip_background)
        backgroundView.elevation = context.resources.getDimension(elevation)

        setupTooltipBehaviour(context, layout, tooltipWidth)

        val x: Float = screenPos[0] - tooltipWidth / 2f - horizontalMargin + anchorView.width / 2f
        val y: Float = (screenPos[1] - estimatedTooltipHeight - verticalMargin - (if (arrow) context.resources.getDimensionPixelSize(R.dimen.dimens_widget_popup_tooltip_arrow_height) else 0)).toFloat()

        //Handle arrow
        if (arrow) setupArrow(layout, context, tooltipArrowWidth, screenPos[0])

        //Handle timed dismiss
        if (!indefinite) {
            handler.postDelayed({ clearTooltipIfNotNull() }, showDuration)
        }

        showTooltipAtLocation(x, y, displayFrame.top, screenPos[0], tooltipWidth, context)
    }

    /**
     * If the arrow flag is set to true, this method enables the arrow and places it at the
     * required place, at the top center of the anchor view.
     */
    private fun setupArrow(layout: View, context: Context, tooltipArrowWidth: Float, anchorViewAbsoluteX: Int) {
        val arrow = layout.findViewById<ImageView>(R.id.layout_widget_popup_tooltip_arrow)
        val stroke = layout.findViewById<ImageView>(R.id.layout_widget_popup_tooltip_arrow_stroke)

        arrow.setImageDrawable(context.getDrawable(arrowDrawable))
        arrow.visibility = View.VISIBLE
        arrow.elevation = context.resources.getDimension(elevation)

        stroke.setImageDrawable(context.getDrawable(arrowDrawable))
        stroke.setColorFilter(arrowStrokeColor)
        stroke.visibility = View.VISIBLE
        stroke.elevation = context.resources.getDimension(elevation)

        arrow.afterMeasured {
            val arrowScreenPositions = IntArray(2)
            arrow.getLocationOnScreen(arrowScreenPositions)

            val arrowAbsoluteX: Float = anchorViewAbsoluteX + anchorView.width / 2f - arrowScreenPositions[0].toFloat() - tooltipArrowWidth / 2f
            arrow.x = arrowAbsoluteX
            stroke.x = arrowAbsoluteX
        }
    }

    /**
     * Increases the clickable area of the view by using a touch delegate.
     */
    private fun setupTouchDelegate(anchorView: View, context: Context) {
        val parent = anchorView.parent as View
        val increaseAmount = context.resources.getDimensionPixelSize(touchDelegateTouchAreaIncreaseAmount)

        // Post in the parent's message queue to make sure the parent
        // lays out its children before we call getHitRect()
        parent.post {
            val touchableArea = Rect()
            anchorView.getHitRect(touchableArea)
            touchableArea.top -= increaseAmount
            touchableArea.bottom += increaseAmount
            touchableArea.left -= increaseAmount
            touchableArea.right += increaseAmount
            parent.touchDelegate = TouchDelegate(touchableArea, anchorView)
        }
    }

    /**
     * Sets up the tooltip behaviour.
     */
    private fun setupTooltipBehaviour(context: Context, layout: View, tooltipWidth: Int) {
        tooltip = PopupWindow(context)

        tooltip?.apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = tooltipWidth

            //Set the custom content view
            contentView = layout

            //Set the background as transparent.
            setBackgroundDrawable(ColorDrawable(context.resources.getColor(android.R.color.transparent, null)))

            //Is outside touch dismissible?
            isOutsideTouchable = outsideTouchDismissible

            //Is inside touch dismissible?
            isTouchable = insideTouchDismissible

            //Is the view focusable? Check modal() setter method for a description.
            isFocusable = modal

            //Is inside touch dismissible
            if (insideTouchDismissible) {
                contentView?.setOnClickListener { dismiss() }
            }

            //Clear the tooltip reference on dismiss.
            setOnDismissListener { tooltip = null }

            //Show animation
            animationStyle = animation

            //Allow to draw outside of the screen.
            isClippingEnabled = true
        }
    }

    /**
     * Shows the tooltip at a specific place with the pre-defined Gravity in the companion object.
     */
    private fun showTooltipAtLocation(x: Float, y: Float, statusBarHeight: Int, anchorXScreenPos: Int, tooltipWidth: Int, context: Context) {
        val displayMetrics = DisplayMetrics()
        context.windowManager.defaultDisplay.getMetrics(displayMetrics)

        val screenWidth = displayMetrics.widthPixels
        val tooltipMaxWidth = anchorXScreenPos + anchorView.width / 2f + tooltipWidth / 2f

        var auxX = x.toInt()
        var auxY = y.toInt()

        //If the tooltip exceeds maxX clamp it to the screen with the horizontal margin.
        if (tooltipMaxWidth >= screenWidth) {
            auxX = screenWidth - tooltipWidth - horizontalMargin - horizontalMarginClipped
        }
        //If the tooltip is drawn out of the screen from the left side clamp it to the screen with the horizontal margin.
        if (anchorXScreenPos - anchorView.width / 2f - tooltipWidth / 2f <= 0f) {
            auxX = horizontalMargin + horizontalMarginClipped
        }

        //If the tooltip exceeds the top bound, clamp it as well.
        if (y - statusBarHeight <= 0f) {
            auxY = verticalMargin + statusBarHeight + verticalMarginClipped
        }

        //If the tooltip exceeds the screen bounds, clip it to the end of the screen.
        tooltip?.showAtLocation(anchorView, GRAVITY, auxX, auxY)

    }
}