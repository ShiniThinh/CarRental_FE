package com.example.carrental_fe.screen.resetPassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.carrental_fe.R
import com.example.carrental_fe.screen.component.BackButton
import com.example.carrental_fe.screen.component.CustomButton
import com.example.carrental_fe.screen.component.InputField
import com.example.carrental_fe.screen.component.PasswordField

@Composable
fun ResetPasswordScreen(verifyClick:()-> Unit)
{
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White, shape = RoundedCornerShape(20.dp))
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.Start
        ){
            Spacer(modifier = Modifier.height(50.dp))

            BackButton(onClick = {}, iconResId = R.drawable.back_icon)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Reset Password",
                fontSize = 28.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_bold)),
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Please enter your Verification Code \n and New Password ",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_regular)),
                color = Color(0xFF707B81),
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Verification Code",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            InputField(placeHolder = "xxxxxx", onValueChange = {})
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = "Password",
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.montserrat_medium)),
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            PasswordField(togglePassWordVisibility = {}, onValueChange = {})
            Spacer(modifier = Modifier.height(50.dp))
            CustomButton(
                onClickChange = verifyClick, text = "Confirm", textColor = 0xFFFFFFFF,
                backgroundColor = Color(0xFF0D6EFD),
                imageResId = null
            )
        }
    }
}

@Preview
@Composable
fun PreviewResetPasswordScreen()
{
    ResetPasswordScreen({})
}