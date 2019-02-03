package com.eric.alodjinha.features.product

import com.eric.alodjinha.base.ioThread
import com.eric.alodjinha.features.product.api.ProductInteractorImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class ProductsFragmentPresenterImpl(val view: ProductsFragmentView) : ProductsFragmentPresenter {

    val interactor = ProductInteractorImpl.instance
    val disposable = CompositeDisposable()
    var categoriaId : Int? = null

    override fun onCreate(categoriaId: Int) {

        this.categoriaId = categoriaId
        view.configureViews()
        getProductsByCategory(0, 20, categoriaId)
    }

    override fun onDestroy() {

        disposable.dispose()
    }

    override fun loadMoreProducts(offset: Int, limite: Int) {

        getProductsByCategory(offset, limite, categoriaId!!)
    }

    private fun getProductsByCategory(offset: Int, limite: Int, categoriaId: Int) {

        view.showLoading()

        interactor.getProductsByCategory(offset, limite, categoriaId)
            .ioThread()
            .doOnSubscribe { }
            .doOnTerminate { view.hideLoading() }
            .subscribe({ view.receiveProducts(it.data) }, {

                // TODO
            }).addTo(disposable)
    }
}