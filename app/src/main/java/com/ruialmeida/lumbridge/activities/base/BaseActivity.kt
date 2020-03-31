package com.ruialmeida.lumbridge.activities.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.ruialmeida.lumbridge.viewmodel.base.BaseViewModel

abstract class BaseActivity<Binding: ViewDataBinding, ViewModel : BaseViewModel<State>, State, ErrorState>: AppCompatActivity() {
    protected lateinit var binding: Binding
    protected lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        innerSetupDataBinding()
        innerSetupViewModel()

        //For obvious reasons, this needs to be called after we setup
        //the data binding.
        setContentView(binding.root)

        setupClickListeners()
        setupContent()
        setupObservables()
    }

    abstract fun setupClickListeners()
    abstract fun setupContent()
    abstract fun setupObservables()
    abstract fun setupDataBinding() : Binding
    abstract fun setupViewModel(): ViewModel

    abstract fun handleUpdateScreenFromState(newState: State?)
    abstract fun handleUpdateScreenFromError(errorState: ErrorState?)

    private fun innerSetupDataBinding() {
        binding = setupDataBinding()
        binding.lifecycleOwner = this
    }

    private fun innerSetupViewModel() {
        viewModel = setupViewModel()
    }
}