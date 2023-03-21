import SwiftUI
import shared

struct RocketLaunchRow: View {
    var missionName: String
    var launchSuccess: KotlinBoolean?
    var launchYear: Int32
    var details: String?
    
    var body: some View {
        HStack() {
            VStack(alignment: .leading, spacing: 10.0) {
                Text("Launch name: \(missionName)")
                Text(launchText).foregroundColor(launchColor)
                Text("Launch year: \(String(launchYear))")
                Text("Launch details: \(details ?? "")")
            }
            Spacer()
        }
    }
}

extension RocketLaunchRow {
    private var launchText: String {
        if let isSuccess = launchSuccess {
            return isSuccess.boolValue ? "Successful" : "Unsuccessful"
        } else {
            return "No data"
        }
    }
    
    private var launchColor: Color {
        if let isSuccess = launchSuccess {
            return isSuccess.boolValue ? Color.green : Color.red
        } else {
            return Color.gray
        }
    }
}

struct RocketLaunchRow_Previews: PreviewProvider {
    static var previews: some View {
        RocketLaunchRow(
            missionName: "FalconSat",
            launchSuccess: false,
            launchYear: 2006,
            details: ""
        )
    }
}
