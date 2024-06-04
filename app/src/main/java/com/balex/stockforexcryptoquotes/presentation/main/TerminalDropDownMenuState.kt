package com.balex.stockforexcryptoquotes.presentation.main

import android.os.Parcelable
import com.balex.stockforexcryptoquotes.domain.entity.Asset
import com.balex.stockforexcryptoquotes.domain.entity.AssetList
import kotlinx.parcelize.Parcelize

@Parcelize
data class TerminalDropDownMenuState(
    val selectedOption: AssetList = AssetList.STOCKS,
    val selectedAsset: Asset = Asset.DEFAULT_STOCK,
    ): Parcelable