## Popup Tooltip Widget
Simple popup tooltip that can be displayed above items.

Example usage: 

```java
        val popupTooltip = PopupTooltip(
                anchorView,
                "My custom popup message",
                layout = R.layout.tooltip_layout,
                textViewId = R.id.tooltip_text,
                tooltipBackgroundId = R.id.tooltip_background
        )
                .horizontalMarginClipped(anchorView.context.resources.getDimension(R.dimen.half_margin).toInt())
                .indefinite(true)
                .arrow(true)
                .arrowBackgroundId(R.id.tooltip_arrow)
                .arrowStrokeColor(android.R.color.white)
                .arrowStrokeId(R.id.tooltip_arrow_stroke)
                .arrowDrawable(R.drawable.tooltip_arrow_down)
                .elevation(R.dimen.default_elevation)
                .touchDelegateTouchAreaIncreaseAmount(10)
                .animationStyle(R.style.popup_window_animation)
                .(...)

        anchorView.setOnClickListener {
            popupTooltip.show()
        }
```

We need to provide an anchor view for the popup to attach to, a message and a layout. Besides,
we also need to provide the id for each view: background, textview, arrow background, so we can
edit them programmatically and do the calculations properly. Only the background of the tooltip is 
mandatory.