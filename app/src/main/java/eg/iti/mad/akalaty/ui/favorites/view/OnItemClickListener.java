package eg.iti.mad.akalaty.ui.favorites.view;

import eg.iti.mad.akalaty.model.SingleMealItem;

public interface OnItemClickListener {
    void onRemoveClicked(SingleMealItem singleMealItem);
    void onItemClicked(SingleMealItem singleMealItem);
}
