import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.shape.RoundedCornerShape
import com.yourapp.ui.components.CountryItem

@Composable
fun CountryPickerDialog(
    isOpen: Boolean,
    countryList: List<CountryItem>,
    onDismiss: () -> Unit,
    onCountrySelected: (CountryItem) -> Unit
) {
    if (!isOpen) return

    Dialog(onDismissRequest = onDismiss) {

        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            tonalElevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 500.dp)   // batas dialog saja
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {

                Text("Select Country", fontSize = 20.sp)

                Spacer(Modifier.height(12.dp))

                // ⭐ BAGIAN YANG BIKIN SCROLL BERFUNGSI ⭐
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)   // INI YANG BENER
                ) {
                    items(countryList) { country ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onCountrySelected(country)
                                    onDismiss()
                                }
                                .padding(vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(country.flag, fontSize = 22.sp)
                            Spacer(Modifier.width(12.dp))

                            Column {
                                Text(country.name)
                                Text(country.code, fontSize = 12.sp, color = Color.Gray)
                            }
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                TextButton(
                    modifier = Modifier.align(Alignment.End),
                    onClick = onDismiss
                ) {
                    Text("Close")
                }
            }
        }
    }
}