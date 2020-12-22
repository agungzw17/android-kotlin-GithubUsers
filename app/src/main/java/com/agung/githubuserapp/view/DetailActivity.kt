package com.agung.githubuserapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.agung.githubuserapp.R
import com.agung.githubuserapp.adapter.SectionsPagerAdapter
import com.agung.githubuserapp.model.GithubUser
import com.agung.githubuserapp.viewmodel.MainDetailViewModel
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {
    private lateinit var mainDetailViewModel: MainDetailViewModel
    private var githubUser = GithubUser() as GithubUser?

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        showDetailGithubUser()
    }

    private fun showDetailGithubUser() {
        val extraGithubUser = intent.getParcelableExtra(EXTRA_GITHUB_USER) as GithubUser?

        val actionbar = supportActionBar
        actionbar!!.title = extraGithubUser?.username
        actionbar.setDisplayHomeAsUpEnabled(true)

        val tvName: TextView = findViewById(R.id.tv_item_name)
        val tvFollower: TextView = findViewById(R.id.tv_item_followers)
        val tvFollowing: TextView = findViewById(R.id.tv_item_following)
        val tvRepositories: TextView = findViewById(R.id.tv_item_repositories)
        val tvGithubUrl: TextView = findViewById(R.id.tv_item_githubUrl)
        val imgPhoto: ImageView = findViewById(R.id.tv_item_image)

        Glide.with(this).load(extraGithubUser?.avatar).into(imgPhoto)
        tvName.text = extraGithubUser?.username

        val getUsername = extraGithubUser?.username.toString()
        mainDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainDetailViewModel::class.java)
        mainDetailViewModel.setGithubUsers(applicationContext, getUsername)

        mainDetailViewModel.getGithubUsers().observe(this, Observer { githubUserItems ->
            tvFollower.text = githubUserItems.followers.toString()
            tvFollowing.text = githubUserItems.following.toString()
            tvRepositories.text = githubUserItems.repositories.toString()
            tvGithubUrl.text = githubUserItems.githubUrl
        })

        showSectionPagerAdapter(getUsername)
    }

    private fun showSectionPagerAdapter(username: String) {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
    }
}