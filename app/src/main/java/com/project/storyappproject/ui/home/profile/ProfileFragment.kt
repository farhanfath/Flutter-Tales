package com.project.storyappproject.ui.home.profile

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.storyappproject.data.datastore.UserPreference
import com.project.storyappproject.data.model.user.UserModel
import com.project.storyappproject.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var userPrefModel : UserPreference
    private lateinit var userModel: UserModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        userPrefModel = UserPreference(requireContext())
        userModel = userPrefModel.getUser()

        setProfileView()
        animationHandler()

        return root
    }

    override fun onResume() {
        super.onResume()
        animationHandler()
    }

    private fun setProfileView() {
        binding.nameTv.text  = userModel.name
        binding.userIdTv.text = userModel.userId
    }

    private fun animationHandler() {
        binding.apply {

            userProfilePict.alpha = 0f
            nameCv.alpha = 0f
            userIdCv.alpha = 0f

            val profilePictAnimator = ObjectAnimator.ofFloat(userProfilePict, View.ALPHA, 0f, 1f)
            profilePictAnimator.duration = 700

            val nameCvAnimator = ObjectAnimator.ofFloat(nameCv, View.ALPHA, 0f, 1f)
            nameCvAnimator.duration = 700

            val userIdCvAnimator = ObjectAnimator.ofFloat(userIdCv, View.ALPHA, 0f, 1f)
            userIdCvAnimator.duration = 700

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(
                profilePictAnimator,
                nameCvAnimator,
                userIdCvAnimator
            )

            animatorSet.start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}