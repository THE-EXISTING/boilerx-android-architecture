package com.existing.boilerx.app.module.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.existing.nextwork.engine.model.NextworkStatus
import com.existing.boilerx.app.R
import com.existing.boilerx.app.module.main.adapter.holder.LoadMoreHolder
import com.existing.boilerx.app.module.main.adapter.holder.PhotoViewHolder
import com.existing.boilerx.app.module.main.adapter.item.PhotoItem
import com.existing.boilerx.app.module.photo.PhotoActivity
import com.existing.boilerx.app.repository.model.PhotoModel
import com.existing.boilerx.common.base.mvvm.AppMvvmRecyclerViewActivity
import com.existing.boilerx.common.base.repository.base.model.AppResult
import com.existing.boilerx.common.snackbar.showSnackbarError
import com.existing.boilerx.core.base.delegate.AdapterHelperDelegate
import com.existing.boilerx.core.base.mvvm.view.adapter.BaseLoadmoreListAdapter
import com.existing.boilerx.core.base.view.holder.base.BaseItem
import com.existing.boilerx.core.base.view.holder.base.BaseViewHolder
import com.existing.boilerx.ktx.delay
import com.existing.boilerx.ktx.isTablet
import com.existing.boilerx.ktx.subscribeOnSuccess
import com.existing.boilerx.ktx.view.setPaddingBottom
import com.thekhaeng.pushdownanim.PushDownAnim
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_holder_picture.view.*
import kotlinx.android.synthetic.main.activity_main.ic_reload as icReload
import kotlinx.android.synthetic.main.activity_main.rv_picture as rvPhoto
import kotlinx.android.synthetic.main.activity_main.swipe_layout as swipeLayout


class MainActivity : AppMvvmRecyclerViewActivity<BaseViewHolder>() {

    companion object {
        const val TYPE_PICTURE_LIST = 10
        const val TYPE_PICTURE = 12


        private const val SPAN_TWO = 2
        private const val SPAN_THREE = 3
    }

    private lateinit var viewModel: MainViewModel
    val preloadSizeProvider = ViewPreloadSizeProvider<PhotoItem>()


    override
    fun onSetupViewModel(savedInstanceState: Bundle?) {
        super.onSetupViewModel(savedInstanceState)
        viewModel = getViewModel(MainViewModel::class.java).apply {
            mapDefaultErrorHandling(errorMessage)
                .observe(this@MainActivity, observerError)
            loadPhotoList.observe(this@MainActivity, observerPictureList)
            loadPhotoListAfterId.observe(this@MainActivity, observerPictureListAfterList)
            loadPhotoListBeforeId.observe(this@MainActivity, observerPictureListBeforeList)
        }
    }

    override
    fun setupLayoutView(): Int = R.layout.activity_main

    override
    fun setupRecyclerView(): RecyclerView? = rvPhoto


    override
    fun registerItemList(): MutableList<BaseItem> = viewModel.baseItemList

    override
    fun onSetupView(savedInstanceState: Bundle?) {
        super.onSetupView(savedInstanceState)
        setSupportActionBar(toolbar)
        PushDownAnim
            .setPushDownAnimTo(fab)
            .setOnClickListener(onClickListener)
            .setScale(0.95f)
        icReload.setOnClickListener(onClickListener)
        swipeLayout.setOnRefreshListener(onRefreshListener)
        setOnLoadMoreListener(onLoadMore, 3)
    }

    override
    fun onSetupLayoutManager(): RecyclerView.LayoutManager {
        return StaggeredGridLayoutManager(
            getSpan(),
            StaggeredGridLayoutManager.VERTICAL
        ).apply {
            gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        }
    }

    override
    fun onSetupRecyclerView(recyclerView: RecyclerView?, adapter: AdapterHelperDelegate.Adapter<BaseViewHolder>) {
        super.onSetupRecyclerView(recyclerView, adapter)
        setOnApplyWindowInsetsListener(rootView) { _, windowInsets ->
            val navBarHeight = windowInsets.systemWindowInsetBottom
            rvPhoto.setPaddingBottom(navBarHeight)
            windowInsets //pass insets down to child view
        }
        rvPhoto.setHasFixedSize(true)
    }

    override
    fun onPrepareInstanceState() {
        super.onPrepareInstanceState()
        viewModel.initialLoadPhoto()
    }

    override
    fun getItemCount(): Int = viewModel.getItemListSize().plus(1)

    override
    fun getItemId(position: Int): Long {
        return viewModel.getItem(position).id
    }

    override
    fun getItemViewType(position: Int): Int {
        return viewModel.getItemType(position)
    }

    override
    fun onCreateLoadmoreViewHolder(parent: ViewGroup): BaseViewHolder {
        return LoadMoreHolder(parent).apply {
            val layoutParams = itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
            layoutParams.isFullSpan = true
        }
    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (viewType == TYPE_PICTURE) {
            return PhotoViewHolder(parent).apply {
                this.clickListener = clickHolderItemListener
            }
        }
        return super.onCreateViewHolder(parent, viewType)
    }

    override
    fun onBindViewHolder(itemList: List<*>, holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(itemList, holder, position)
        if (getItemViewType(position) == TYPE_PICTURE) {
            val item = viewModel.getItem(position) as PhotoItem
            val viewHolder = holder as PhotoViewHolder
            viewHolder.onBind(item)
            preloadSizeProvider.setView(viewHolder.itemView.image)
        }
    }

    /* =========================== Private method =============================================== */
    private fun showRefreshing() {
        icReload.isEnabled = false
        swipeLayout.isRefreshing = true
    }

    private fun hideRefreshing() {
        icReload.isEnabled = true
        delay({ swipeLayout.isRefreshing = false }, 1500)
    }


    private fun openPhotoActivity(id: Long) {
        val data = bundleOf(PhotoActivity.KEY_PHOTO_ID to id)
        openActivity(PhotoActivity::class.java, data = data)
    }


    private fun getSpan(): Int {
        return when {
            isTablet -> SPAN_THREE
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE -> SPAN_THREE
            resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT -> SPAN_TWO
            else -> SPAN_TWO
        }
    }

    /* =========================== Callback method ============================================== */
    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v) {
            fab -> rvPhoto.smoothScrollToPosition(0)
            icReload -> {
                showRefreshing()
                val result = calculateDiffUtil(emptyList())
                viewModel.refreshData()
                notifyDataSetChangedWithDiffUtil(result)
            }
        }
    }

    private val clickHolderItemListener: BaseViewHolder.OnClickListener = object : BaseViewHolder.OnClickListener {
        override
        fun onClick(view: View?, position: Int) {
            openPhotoActivity(viewModel.getItem(position).id)
        }
    }

    private val onRefreshListener: SwipeRefreshLayout.OnRefreshListener =
        SwipeRefreshLayout.OnRefreshListener {
            viewModel.refreshData()
            notifyDataSetChanged()
        }


    private val onLoadMore = object : BaseLoadmoreListAdapter.OnLoadMoreListener {
        override
        fun onLoadMore() {
            viewModel.loadMoreData()
        }
    }


    private val observerPictureList: Observer<AppResult<List<PhotoModel>>?> =
        Observer { resource: AppResult<List<PhotoModel>>? ->
            when {
                resource?.status == NextworkStatus.LOADING_FROM_DATABASE -> {
                    // do nothing
                }
                resource?.status == NextworkStatus.SUCCESS -> {
                    resource.data?.let {
                        val newDataList = viewModel.getNewDataList(it)
                        calculateDiffUtilAsync(newDataList)
                            .subscribeOnSuccess { result ->
                                showLoadmore(false)
                                hideRefreshing()
                                viewModel.updatePhotoItemList(newDataList)
                                notifyDataSetChangedWithDiffUtil(result)
                            }
                    }
                }
                resource?.status == NextworkStatus.LOADING_FROM_NETWORK -> {
                    showRefreshing()
                }
            }
        }

    private val observerPictureListBeforeList: Observer<AppResult<List<PhotoModel>>?> =
        Observer { resource: AppResult<List<PhotoModel>>? ->
            when {
                resource?.status == NextworkStatus.LOADING_FROM_DATABASE -> {
                    // do nothing
                }
                resource?.status == NextworkStatus.SUCCESS -> {
                    resource.data?.let {
                        val newDataList = viewModel.getBeforeNewDataList(it)
                        calculateDiffUtilAsync(newDataList)
                            .subscribeOnSuccess { result ->
                                showLoadmore(false)
                                hideRefreshing()
                                viewModel.updatePhotoItemList(newDataList)
                                notifyDataSetChangedWithDiffUtil(result)
                            }
                    }
                }
                resource?.status == NextworkStatus.LOADING_FROM_NETWORK -> {
                    showLoadmore(true)
                }
            }
        }


    private val observerPictureListAfterList: Observer<AppResult<List<PhotoModel>>?> =
        Observer { resource: AppResult<List<PhotoModel>>? ->
            when {
                resource?.status == NextworkStatus.LOADING_FROM_DATABASE -> {
                    // do nothing
                }
                resource?.status == NextworkStatus.SUCCESS -> {
                    resource.data?.let {
                        val newDataList = viewModel.getAfterNewDataList(it)
                        calculateDiffUtilAsync(newDataList)
                            .subscribeOnSuccess { result ->
                                showLoadmore(false)
                                hideRefreshing()
                                viewModel.updatePhotoItemList(newDataList)
                                notifyDataSetChangedWithDiffUtil(result)
                            }
                    }
                }
                resource?.status == NextworkStatus.LOADING_FROM_NETWORK -> {
                    // do nothing
                }
            }
        }

    private val observerError: Observer<Throwable?> =
        Observer { throwable ->
            throwable?.message?.let { showSnackbarError(it) }
            showLoadmore(false)
            hideRefreshing()
        }
}
