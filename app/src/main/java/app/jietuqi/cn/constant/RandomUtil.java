package app.jietuqi.cn.constant;

import app.jietuqi.cn.ResourceHelper;
import app.jietuqi.cn.util.TimeUtil;

/**
 * 作者： liuyuanbo on 2018/10/15 12:16.
 * 时间： 2018/10/15 12:16
 * 邮箱： 972383753@qq.com
 * 用途：
 */

public class RandomUtil {
    public static final String[] randomNick = {
            "A奢侈品代购", "手机维修", "剪刀", "JAB", "间隔",
            "大宝", "简爱", "花儿和少年", "慌乱", "花开ヽ似水￠",
            "新手练习", "Lucky", "So Cute", "勿忘心安", "博哥哥",
            "约定", "一定是", "特别的缘分", "才可以", "一路走来",
            "变成了一家人", "他多爱你积分", "就多伤我积分", "A护肤品代购", "小猪"};

    public static final int[] randomAvatar = {
            ResourceHelper.getAppIconId("R.mipmap.role1"), ResourceHelper.getAppIconId("R.mipmap.role14"),
            ResourceHelper.getAppIconId("R.mipmap.role2"),
            ResourceHelper.getAppIconId("R.mipmap.role3"), ResourceHelper.getAppIconId("R.mipmap.role15"),
            ResourceHelper.getAppIconId("R.mipmap.role4"), ResourceHelper.getAppIconId("R.mipmap.role16"),
            ResourceHelper.getAppIconId("R.mipmap.role5"), ResourceHelper.getAppIconId("R.mipmap.role17"),
            ResourceHelper.getAppIconId("R.mipmap.role6"), ResourceHelper.getAppIconId("R.mipmap.role18"),
            ResourceHelper.getAppIconId("R.mipmap.role7"), ResourceHelper.getAppIconId("R.mipmap.role19"),
            ResourceHelper.getAppIconId("R.mipmap.role8"), ResourceHelper.getAppIconId("R.mipmap.role20"),
            ResourceHelper.getAppIconId("R.mipmap.role9"), ResourceHelper.getAppIconId("R.mipmap.role21"),
            ResourceHelper.getAppIconId("R.mipmap.role10"), ResourceHelper.getAppIconId("R.mipmap.role22"),
            ResourceHelper.getAppIconId("R.mipmap.role11"), ResourceHelper.getAppIconId("R.mipmap.role23"),
            ResourceHelper.getAppIconId("R.mipmap.role12"), ResourceHelper.getAppIconId("R.mipmap.role24"),
            ResourceHelper.getAppIconId("R.mipmap.role13"), ResourceHelper.getAppIconId("R.mipmap.role25")};
    public static final String[] randomAvatarName = {
            "R.mipmap.role1", "R.mipmap.role2",
            "R.mipmap.role3",
            "R.mipmap.role4", "R.mipmap.role5",
            "R.mipmap.role6", "R.mipmap.role7",
            "R.mipmap.role8", "R.mipmap.role9",
            "R.mipmap.role10", "R.mipmap.role11",
            "R.mipmap.role12", "R.mipmap.role13",
            "R.mipmap.role14", "R.mipmap.role15",
            "R.mipmap.role16", "R.mipmap.role17",
            "R.mipmap.role18", "R.mipmap.role19",
            "R.mipmap.role20", "R.mipmap.role21",
            "R.mipmap.role22", "R.mipmap.role23",
            "R.mipmap.role24", "R.mipmap.role25"};
    /**
     * 微信添加新朋友
     */
    public static final String[] newFriendsAvatar = {
            "R.mipmap.role1", "R.mipmap.role2", "R.mipmap.role3", "R.mipmap.role4", "R.mipmap.role5",
            "R.mipmap.role6", "R.mipmap.role7", "R.mipmap.role8", "R.mipmap.role9", "R.mipmap.role10",
            "R.mipmap.role11", "R.mipmap.role12", "R.mipmap.role13", "R.mipmap.role14", "R.mipmap.role15",
            "R.mipmap.role16", "R.mipmap.role17", "R.mipmap.role18", "R.mipmap.role19", "R.mipmap.role20",
            "R.mipmap.role21", "R.mipmap.role22", "R.mipmap.role23", "R.mipmap.role24", "R.mipmap.role25",
            "R.mipmap.role26", "R.mipmap.role27", "R.mipmap.role28", "R.mipmap.role29", "R.mipmap.role30",
            "R.mipmap.role31", "R.mipmap.role32", "R.mipmap.role33", "R.mipmap.role34", "R.mipmap.role35",
            "R.mipmap.role36", "R.mipmap.role37", "R.mipmap.role38", "R.mipmap.role39", "R.mipmap.role40",
            "R.mipmap.role41", "R.mipmap.role42", "R.mipmap.role43", "R.mipmap.role44", "R.mipmap.role45",
            "R.mipmap.role46", "R.mipmap.role47", "R.mipmap.role48", "R.mipmap.role49", "R.mipmap.role50",
            "R.mipmap.role51", "R.mipmap.role52", "R.mipmap.role53", "R.mipmap.role54", "R.mipmap.role55",
            "R.mipmap.role56", "R.mipmap.role57", "R.mipmap.role58", "R.mipmap.role59", "R.mipmap.role60",
            "R.mipmap.role61", "R.mipmap.role62", "R.mipmap.role63", "R.mipmap.role64", "R.mipmap.role65",
            "R.mipmap.role66", "R.mipmap.role67", "R.mipmap.role68", "R.mipmap.role69", "R.mipmap.role70",
            "R.mipmap.role71", "R.mipmap.role72", "R.mipmap.role73", "R.mipmap.role74", "R.mipmap.role75",
            "R.mipmap.role76", "R.mipmap.role77", "R.mipmap.role78", "R.mipmap.role79", "R.mipmap.role80",
            "R.mipmap.role81", "R.mipmap.role82", "R.mipmap.role83", "R.mipmap.role84", "R.mipmap.role85",
            "R.mipmap.role86", "R.mipmap.role87", "R.mipmap.role88", "R.mipmap.role89", "R.mipmap.role90",
            "R.mipmap.role91", "R.mipmap.role92", "R.mipmap.role93", "R.mipmap.role94", "R.mipmap.role95",
            "R.mipmap.role96", "R.mipmap.role97", "R.mipmap.role98", "R.mipmap.role99", "R.mipmap.role100",
            "R.mipmap.role101", "R.mipmap.role102", "R.mipmap.role103", "R.mipmap.role104", "R.mipmap.role105",
            "R.mipmap.role106", "R.mipmap.role107", "R.mipmap.role108", "R.mipmap.role109", "R.mipmap.role110",
            "R.mipmap.role111", "R.mipmap.role112", "R.mipmap.role113", "R.mipmap.role114", "R.mipmap.role115",
            "R.mipmap.role116", "R.mipmap.role117", "R.mipmap.role118", "R.mipmap.role119", "R.mipmap.role120",
            "R.mipmap.role121", "R.mipmap.role122", "R.mipmap.role123", "R.mipmap.role124", "R.mipmap.role125",
            "R.mipmap.role126", "R.mipmap.role127", "R.mipmap.role128", "R.mipmap.role129", "R.mipmap.role130",
            "R.mipmap.role131", "R.mipmap.role132", "R.mipmap.role133", "R.mipmap.role134", "R.mipmap.role135",
            "R.mipmap.role136", "R.mipmap.role137", "R.mipmap.role138", "R.mipmap.role139", "R.mipmap.role140",
            "R.mipmap.role141", "R.mipmap.role142", "R.mipmap.role143", "R.mipmap.role144", "R.mipmap.role145",
            "R.mipmap.role146", "R.mipmap.role147", "R.mipmap.role148", "R.mipmap.role149", "R.mipmap.role150",
            "R.mipmap.role151", "R.mipmap.role152", "R.mipmap.role153", "R.mipmap.role154", "R.mipmap.role155",
            "R.mipmap.role156", "R.mipmap.role157", "R.mipmap.role158", "R.mipmap.role159", "R.mipmap.role160",
            "R.mipmap.role161", "R.mipmap.role162", "R.mipmap.role163", "R.mipmap.role164", "R.mipmap.role165",
            "R.mipmap.role166", "R.mipmap.role167", "R.mipmap.role168", "R.mipmap.role169", "R.mipmap.role170",
            "R.mipmap.role171", "R.mipmap.role172", "R.mipmap.role173", "R.mipmap.role174", "R.mipmap.role175",
            "R.mipmap.role176", "R.mipmap.role177", "R.mipmap.role178", "R.mipmap.role179", "R.mipmap.role180",
            "R.mipmap.role181", "R.mipmap.role182", "R.mipmap.role183", "R.mipmap.role184", "R.mipmap.role185",
            "R.mipmap.role186", "R.mipmap.role187", "R.mipmap.role188", "R.mipmap.role189", "R.mipmap.role190",
            "R.mipmap.role191", "R.mipmap.role192", "R.mipmap.role193", "R.mipmap.role194", "R.mipmap.role195",
            "R.mipmap.role196", "R.mipmap.role197", "R.mipmap.role198", "R.mipmap.role199", "R.mipmap.role200",
            "R.mipmap.role201", "R.mipmap.role202", "R.mipmap.role203", "R.mipmap.role204", "R.mipmap.role205",
            "R.mipmap.role206", "R.mipmap.role207", "R.mipmap.role208", "R.mipmap.role209", "R.mipmap.role210",
            "R.mipmap.role211", "R.mipmap.role212", "R.mipmap.role213", "R.mipmap.role214", "R.mipmap.role215",
            "R.mipmap.role216", "R.mipmap.role217", "R.mipmap.role218", "R.mipmap.role219", "R.mipmap.role220",
            "R.mipmap.role221", "R.mipmap.role222", "R.mipmap.role223", "R.mipmap.role224", "R.mipmap.role225",
            "R.mipmap.role226", "R.mipmap.role227", "R.mipmap.role228", "R.mipmap.role229", "R.mipmap.role230",
            "R.mipmap.role231", "R.mipmap.role232", "R.mipmap.role233", "R.mipmap.role234", "R.mipmap.role235",
            "R.mipmap.role236", "R.mipmap.role237", "R.mipmap.role238", "R.mipmap.role239", "R.mipmap.role240",
            "R.mipmap.role241", "R.mipmap.role242", "R.mipmap.role243", "R.mipmap.role244", "R.mipmap.role245",
            "R.mipmap.role246", "R.mipmap.role247", "R.mipmap.role248", "R.mipmap.role249", "R.mipmap.role250",
            "R.mipmap.role251", "R.mipmap.role252", "R.mipmap.role253", "R.mipmap.role254", "R.mipmap.role255",
            "R.mipmap.role256", "R.mipmap.role257", "R.mipmap.role258", "R.mipmap.role259", "R.mipmap.role260",
            "R.mipmap.role261", "R.mipmap.role262", "R.mipmap.role263", "R.mipmap.role264", "R.mipmap.role265",
            "R.mipmap.role266", "R.mipmap.role267", "R.mipmap.role268", "R.mipmap.role269", "R.mipmap.role270",
            "R.mipmap.role271", "R.mipmap.role272", "R.mipmap.role273", "R.mipmap.role274", "R.mipmap.role275",
            "R.mipmap.role276", "R.mipmap.role277", "R.mipmap.role278", "R.mipmap.role279", "R.mipmap.role280",
            "R.mipmap.role281", "R.mipmap.role282", "R.mipmap.role283", "R.mipmap.role284", "R.mipmap.role285",
            "R.mipmap.role286", "R.mipmap.role287", "R.mipmap.role288", "R.mipmap.role289", "R.mipmap.role290",
            "R.mipmap.role291", "R.mipmap.role292", "R.mipmap.role293", "R.mipmap.role294", "R.mipmap.role295",
            "R.mipmap.role296", "R.mipmap.role297", "R.mipmap.role298", "R.mipmap.role299", "R.mipmap.role300",
            "R.mipmap.role301", "R.mipmap.role302", "R.mipmap.role303", "R.mipmap.role304", "R.mipmap.role305",
            "R.mipmap.role306", "R.mipmap.role307", "R.mipmap.role308", "R.mipmap.role309", "R.mipmap.role310",
            "R.mipmap.role311", "R.mipmap.role312", "R.mipmap.role313", "R.mipmap.role314", "R.mipmap.role315",
            "R.mipmap.role316", "R.mipmap.role317", "R.mipmap.role318", "R.mipmap.role319", "R.mipmap.role320",
            "R.mipmap.role321", "R.mipmap.role322", "R.mipmap.role323", "R.mipmap.role324", "R.mipmap.role325",
            "R.mipmap.role326", "R.mipmap.role327", "R.mipmap.role328", "R.mipmap.role329", "R.mipmap.role330",
            "R.mipmap.role331", "R.mipmap.role332", "R.mipmap.role333", "R.mipmap.role334", "R.mipmap.role335",
            "R.mipmap.role336", "R.mipmap.role337", "R.mipmap.role338", "R.mipmap.role339", "R.mipmap.role340",
            "R.mipmap.role341", "R.mipmap.role342", "R.mipmap.role343", "R.mipmap.role344", "R.mipmap.role345",
            "R.mipmap.role346", "R.mipmap.role347", "R.mipmap.role348", "R.mipmap.role349", "R.mipmap.role350",
            "R.mipmap.role351", "R.mipmap.role352", "R.mipmap.role353", "R.mipmap.role354", "R.mipmap.role355",
            "R.mipmap.role356", "R.mipmap.role357", "R.mipmap.role358", "R.mipmap.role359", "R.mipmap.role360",
            "R.mipmap.role361", "R.mipmap.role362", "R.mipmap.role363", "R.mipmap.role364", "R.mipmap.role365",
            "R.mipmap.role366", "R.mipmap.role367", "R.mipmap.role368", "R.mipmap.role369", "R.mipmap.role370",
            "R.mipmap.role371", "R.mipmap.role372", "R.mipmap.role373", "R.mipmap.role374", "R.mipmap.role375",
            "R.mipmap.role376", "R.mipmap.role377", "R.mipmap.role378", "R.mipmap.role379", "R.mipmap.role380",
            "R.mipmap.role381", "R.mipmap.role382", "R.mipmap.role383", "R.mipmap.role384", "R.mipmap.role385",
            "R.mipmap.role386", "R.mipmap.role387", "R.mipmap.role388", "R.mipmap.role389", "R.mipmap.role390",
            "R.mipmap.role391", "R.mipmap.role392", "R.mipmap.role393", "R.mipmap.role394", "R.mipmap.role395",
            "R.mipmap.role396", "R.mipmap.role397", "R.mipmap.role398", "R.mipmap.role399", "R.mipmap.role400",
            "R.mipmap.role401", "R.mipmap.role402", "R.mipmap.role403", "R.mipmap.role404", "R.mipmap.role405",
            "R.mipmap.role406", "R.mipmap.role407", "R.mipmap.role408", "R.mipmap.role409", "R.mipmap.role410",
            "R.mipmap.role411", "R.mipmap.role412", "R.mipmap.role413", "R.mipmap.role414", "R.mipmap.role415",
            "R.mipmap.role416", "R.mipmap.role417", "R.mipmap.role418", "R.mipmap.role419", "R.mipmap.role420",
            "R.mipmap.role421", "R.mipmap.role422", "R.mipmap.role423", "R.mipmap.role424", "R.mipmap.role425",
            "R.mipmap.role426", "R.mipmap.role427", "R.mipmap.role428", "R.mipmap.role429", "R.mipmap.role430",
            "R.mipmap.role431", "R.mipmap.role432", "R.mipmap.role433", "R.mipmap.role434", "R.mipmap.role435",
            "R.mipmap.role436", "R.mipmap.role437", "R.mipmap.role438", "R.mipmap.role439", "R.mipmap.role440",
            "R.mipmap.role441", "R.mipmap.role442", "R.mipmap.role443", "R.mipmap.role444", "R.mipmap.role445",
            "R.mipmap.role446", "R.mipmap.role447", "R.mipmap.role448", "R.mipmap.role449", "R.mipmap.role450",
            "R.mipmap.role451", "R.mipmap.role452", "R.mipmap.role453", "R.mipmap.role454", "R.mipmap.role455",
            "R.mipmap.role456", "R.mipmap.role457", "R.mipmap.role458", "R.mipmap.role459", "R.mipmap.role460",
            "R.mipmap.role461", "R.mipmap.role462", "R.mipmap.role463", "R.mipmap.role464", "R.mipmap.role465",
            "R.mipmap.role466", "R.mipmap.role467", "R.mipmap.role468", "R.mipmap.role469", "R.mipmap.role470",
            "R.mipmap.role471", "R.mipmap.role472", "R.mipmap.role473", "R.mipmap.role474", "R.mipmap.role475",
            "R.mipmap.role476", "R.mipmap.role477", "R.mipmap.role478", "R.mipmap.role479", "R.mipmap.role480",
            "R.mipmap.role481", "R.mipmap.role482", "R.mipmap.role483", "R.mipmap.role484", "R.mipmap.role485",
            "R.mipmap.role486", "R.mipmap.role487", "R.mipmap.role488", "R.mipmap.role489", "R.mipmap.role490",
            "R.mipmap.role491", "R.mipmap.role492", "R.mipmap.role493", "R.mipmap.role494", "R.mipmap.role495",
            "R.mipmap.role496", "R.mipmap.role497", "R.mipmap.role498", "R.mipmap.role499", "R.mipmap.role500"
    };

    /**
     * 微信新朋友的昵称
     */
    public static final String[] newFriendsNick = {
            "钢琴上的芭蕾 *", "初秋繁花入梦╮", "思念幻化成海", "伊人淡雅泪", "淡意衬优柔丶", "钕人如花ゝ花似梦", "装逼不适合你", "妆饰界", "源于玉", "云香茶叶",
            "下下的小可爱", "缘来小站", "有间精品店", "衣帘幽梦", "星闪缘", "我淘我爱", "原来是礼", "阿道夫", "良子时尚家居家纺店", "复制人生",
            "艾路丝婷", "卓多姿", "红豆家具", "韩版小屋", "海美进口", "桃洛憬","请在乎我１秒","熏染","巴黎盛开的樱花","白恍",
            "秒淘你心窝","柠栀","你身上有刺，别扎我","旧爱剩女","如你所愿","没过试用期的爱~","陌上花","开心的笨小孩","在哪跌倒こ就在哪躺下",
            "限量版女汉子","蝶恋花╮","优雅的叶子","ー半忧伤","君临臣","夏日倾情","闲肆","暖瞳","珠穆郎马疯@","站上冰箱当高冷！",
            "断秋风","拥菢过后只剰凄凉→","安好如初","男神大妈","晨与橙与城","泡泡龙","刺心爱人i","陌然淺笑","龙吟凤","别留遗憾",
            "太易動情也是罪名","烟雨萌萌","伤离别","心贝","嗯咯","病房","罪歌","久爱不厌","々爱被冰凝固ゝ","单身i",
            "人心叵测i","爱你的小笨蛋","封心锁爱","她的风骚姿势我学不来","生命一旅程","(り。薆情海","红尘滚滚","赋流云","余温散尽ぺ","無極卍盜",
            "我就是这样一个人","她最好i","←极§速","窒息","自繩自縛","念安я","陌上蔷薇","拽年很骚","↘▂_倥絔","虚伪了的真心",
            "嘲笑！","北朽暖栀","命该如此","傲世九天","﹏櫻之舞﹏","浅笑√倾城","你瞒我瞒","各自安好ぃ","墨染殇雪","温柔腔",
            "落花忆梦","灵魂摆渡人","莫飞霜","疲倦了","若他只爱我。","南初","不忘初心","浮光浅夏ζ","放血","毒舌妖后",
            "笑傲苍穹","麝香味","糖果控","熟悉看不清","无关风月","孤者何惧","哥帅但不是蟋蟀","哭着哭着就萌了°","回忆未来","心抽搐到严重畸形っ°",
            "莫失莫忘","。婞褔ｖīｐ","安之若素","从此我爱的人都像你","＊巴黎铁塔","海盟山誓总是赊","陌颜幽梦","妄灸","栀蓝","剑已封鞘",
            "青山暮雪","逃避","冷风谷离殇","凉凉凉”凉但是人心","墨城烟柳","身影","隔壁阿不都","空谷幽兰","入骨相思","■□丶一切都无所谓",
            "℡默默的爱","你tm的滚","万象皆为过客","?娘子汉","灬一抹丶苍白","容纳我ii","坠入深海i","巷雨优美回忆","孤城暮雨","把孤独喂饱",
            "裸睡の鱼","冷嘲热讽i","凉城°","醉婉笙歌","〆mè村姑","冷落了♂自己·","初阳","你与清晨阳光","念初","不要冷战i",
            "服装专卖","尘埃落定","Hello爱情风","心脏偷懒","慑人的傲气","情如薄纱","陌颜","堕落爱人！","情深至命","淡抹丶悲伤",
            "有一种中毒叫上瘾成咆哮i","良人凉人","无所谓","一笑傾城゛","恰十年","敬情","来自火星的我","心已麻木i","夏以乔木","强辩",
            "青衫负雪","涐们的幸福像流星丶","▼遗忘那段似水年华","南宫沐风","荒野情趣","瑾澜","水波映月","殃樾晨","素衣青丝","无望的后半生",
            "浅嫣婉语","冷眼旁观i","清羽墨安","汐颜兮梦ヘ","暖栀","你是梦遥不可及","寻鱼水之欢","关于道别","龙吟凤","喜欢梅西",
            "繁华若梦","断桥残雪","有恃无恐","像从了良","墨城烟柳","轨迹！","未来未必来","凝残月","月下独酌","顶个蘑菇闯天下i",
            "非想","不离我","服从","信仰","惯例","旧事酒浓","话少情在","琴断朱弦","漫长の人生","一别经年",
            "命里缺他","终究会走-","如梦初醒","最怕挣扎","哽咽","抓不住i","tina","羁绊你","小清晰的声音","孤街浪途",
            "余笙南吟","早不爱了","半世晨晓。","一样剩余","枫无痕","致命伤","拉扯","心安i","凝残月","采姑娘的小蘑菇",
            "青衫故人","淡写薰衣草的香","景忧丶枫涩帘淞幕雨","風景綫つ","♂你那刺眼的温柔","七级床震","肆忌","∞◆暯小萱◆","孤岛晴空","淡抹烟熏妆丶",
            "巷口酒肆","浅巷°","我没有爱人i","枫无痕","孤独症","有你，很幸福","一尾流莺","哭花了素颜","纯情小火鸡","西瓜贩子",
            "玻璃渣子","青袂婉约","最迷人的危险","等量代换","予之欢颜","清原","米兰","断秋风","打个酱油卖个萌","一点一点把你清,空",
            "盲从于你","长裙绿衣","看我发功喷飞你","怅惘","一笑抵千言","病态的妖孽","我家的爱豆是怪,比i","坏小子不坏","江山策","灼痛",
            "花花世界总是那,么虚伪﹌","秘密","軨倾词","有些人,只适合,好奇~","逾期不候","岁岁年年","忘故","焚心劫","泪湿青衫","安陌醉生",
            "半梦半醒i","炙年","有阳光还感觉冷","迁心","别在我面前犯贱","素婉纤尘","我绝版了i","失心疯i","孤自凉丶","摩天轮的依恋",
            "柠夏初开","雪花ミ飞舞","孤单*无名指","煮酒","苏樱凉","鹿先森，教魔方","仅有的余温","伱德柔情是我的痛。","我怕疼别碰我伤口","醉枫染墨",
            "留我一人","减肥伤身#","笑叹★尘世美","陌潇潇","浮世繁华","满心狼藉","ゞ香草可樂ゞ草莓布丁","执拗旧人","墨染年华","寒山远黛",
            "昂贵的背影","暮染轻纱","素颜倾城","我的黑色迷你裙","代价是折磨╳","爱你心口难开","微信名字","歇火","念旧情i","搞搞嗎妹妹",
            "蹂躏少女","仰望幸福","迷路的男人","青衫故人","见朕骑妓的时刻","愁杀","■孤独像过不去的桥≈","千城暮雪","心悸╰つ","残花为谁悲丶",
            "棃海","纯真ブ已不复存在","幸好是你","放肆丶小侽人","孤廖","相知相惜","眼角有泪°","情字何解ヘ","゛指尖的阳光丶","oО清风挽发oО",
            "折木","爱情的过失","眷恋","还未走i","绝版女子","迷路的男人","十言i","何年何念","颓废人士","花开丶若相惜",
            "◆乱世梦红颜","花海","墨城烟柳","淡抹丶悲伤","爱到伤肺i","何必锁我心","滴在键盘上的泪","命运不堪浮华","凉话刺骨","发呆",
            "微信名字","大王派我来巡山！","来瓶年的冰泉","算了吧","夜晟洛","青墨断笺み","苏莫晨","丢了爱情i","短发","挽手余生ら",
            "此生一诺","此刻不是了i","命硬","拥抱","为爱放弃","闷骚闷出味道了","风中摇曳着长发","七夏i","古巷青灯","追忆思域。",
            "孤败","￡烟消云散","你很爱吃凉皮","莫阑珊","弃我者亡","呆檬","今非昔比","执妄","别伤我i","矜暮",
            "凉月流沐@","空巷","话扎心","我一个人","雨后彩虹","抚涟i","别悲哀","箜明","红玫瑰。","执笔画眉",
            "百合的盛世恋","三年约","沐晴つ","一个人的荒凉","╯念抹浅笑","独角戏°","陌離","回到你身边","在哪跌倒こ就在哪躺下","经典的对白",
            "歆久","黑夜漫长","颜洛殇","◆残留德花瓣","墨染天下","夏至离别","浮殇年华","娇眉恨","疯人疯语疯人愿","金橙橙。-",
            "无寒","败类","野兽之美","烬陌袅","怀念·最初","あ浅浅の嘚僾","暮光薄凉","花菲","冷青裳","烟雨离殇",
            "一纸愁肠。","Edinburgh°南空","日久见人心","酒笙倾凉","淡淡の清香","┲﹊怅惘。","杰克","半梦半醒半疯癫","志平","屌国女农",
            "寂莫","|赤;焰﹏゛","面瘫脸","红尘烟雨","铁树不曾开花","权诈","?亡梦爱人","余温散尽ぺ","遗憾最汹涌","稳妥",
            "念旧是个瘾。","鹿叹","不识爱人心","ヅ她的身影若隐若现","抚笙","你的眸中有星辰","苍白的笑〃","凉笙墨染","封锁感觉","陌上花",
            "那伤。眞美","该用户已上天","伪心","将妓就计","蔚蓝的天空〃没有我的翅膀","巷陌繁花丶","箜篌引","反正是我","一生只盼一人","莫名的青春",
            "北染陌人","聽風","全网暗恋者","走过海棠暮","葵雨","微信名字","惦着脚尖摘太阳","ok绷遮不住我颓废的伤あ","雁過藍天","神经兮兮°",
            "几妆痕","掉眼泪","冷月花魂","太难","花容月貌","颓废i","怪咖","娇眉恨","不相忘", "心亡则人忘"
    };
    public static final String[] newFriendMsg = {
            "请求添加你为朋友","来自手机通讯录好友","能认识下么","你好，朋友介绍","咨询产品","我是","你好啊","在么","加一下，有事找你"
    };
    /**
     * 微信零钱明细 -- 收入
     */
    public static final String[] randomWechatChargeEarningDetail = {"微信红包", "群收款", "充值 ", "微信面对面收钱！", "退款", "微信转账"};
    /**
     * 微信零钱明细 -- 支出
     */
    public static final String[] randomWechatChargeExpenditureDetail = {"微信转账", "微信红包", "群收款", "付款", "生活缴费", "提现手续费", "提现",
            "100元手机话费-3141135309", "美团订单-100000180978345463469527", "百度外卖-148635645856548541202",
            "京东-6014689429782", "爱奇艺VIP会员12个月"};
    /**
     * 对方账号（手机号/邮箱） -- 支出
     */
    public static final String[] randomAccounts = {"13141135309 ", "15919819791", "13517352247", "1281274360@qq.com", "250578978@qq.com", "13141135409@163.com"};
    /**
     * 红包编号需要随机生成一个28位长度的数字字符串
     */
    public static final char[] mNumArray = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    /**
     * 获取随机的名头像
     * @return
     */
    public static int getRandomAvatar(){
        int index = (int) (Math.random() * randomAvatar.length);
        int random = RandomUtil.randomAvatar[index];
        return random;
    }
    /**
     * 获取随机的昵称
     * @return
     */
    public static String getRandomNickName(){
        int index = (int) (Math.random() * randomNick.length);
        String random = RandomUtil.randomNick[index];
        return random;
    }
    /**
     * 按顺序获取昵称
     * @return
     */
    public static String getOrderNickName(int index){
        return RandomUtil.randomNick[index];
    }
    /**
     * 获取随机的名微信朋友头像
     * @return
     */
    public static String getRandomNewFriendAvatar(){
        int index = (int) (Math.random() * newFriendsAvatar.length);
        String random = RandomUtil.newFriendsAvatar[index];
        return random;
    }
    /**
     * 获取随机的名微信朋友昵称
     * @return
     */
    public static String getRandomNewFriendNickName(){
        int index = (int) (Math.random() * newFriendsNick.length);
        String random = RandomUtil.newFriendsNick[index];
        return random;
    }
    /**
     * 获取随机的名微信朋友昵称
     * @return
     */
    public static String getRandomNewFriendMsg(){
        int index = (int) (Math.random() * newFriendMsg.length);
        String random = RandomUtil.newFriendMsg[index];
        return random;
    }
    /**
     * 按顺序获取头像
     * @return
     */
    public static int getOrderAvatar(int index){
        return RandomUtil.randomAvatar[index];
    }
    /**
     * 按顺序获取名称
     * @return
     */
    public static String getRandomAvatarName(int index){
        return RandomUtil.randomAvatarName[index];
    }
    /**
     * 获取随机的收入名称
     * @return
     */
    public static String getRandomChargeEarningDetail(){
        int index = (int) (Math.random() * randomWechatChargeEarningDetail.length);
        String random = RandomUtil.randomWechatChargeEarningDetail[index];
        return random;
    }
    /**
     * 获取随机的支出名称
     * @return
     */
    public static String getRandomChargeExpenditureDetail(){
        int index = (int) (Math.random() * randomWechatChargeExpenditureDetail.length);
        String random = RandomUtil.randomWechatChargeExpenditureDetail[index];
        return random;
    }
    /**
     * 获取随机账号
     * @return
     */
    public static String getRandomAccounts(){
        int index = (int) (Math.random() * randomAccounts.length);
        String random = RandomUtil.randomAccounts[index];
        return random;
    }
    public static String getRandomNum(int size){
        StringBuffer buffer = new StringBuffer(TimeUtil.getNowTimeWithYMD());
        char ch;
        for (int i = 0; i < size; i++) {
            int index = (int) (Math.random() * mNumArray.length);
            ch = mNumArray[index];
            buffer.append(ch);
        }
        return buffer.toString();
    }
}
