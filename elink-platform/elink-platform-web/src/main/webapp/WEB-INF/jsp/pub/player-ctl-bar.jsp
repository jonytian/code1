<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<link rel="stylesheet" href="<c:url value='/css/player-ctl-bar.css'/>" />
<!--控制器-->
<div class="player_ctl_bar">
	<!--进度条容器-->
	<div class="player_ctl_progress_bar" id="player_ctl_progress_bar" title="拖动回放">
		<!--进度条底色-->
		<div class="player_ctl_progress_bar_bg">
			<!--缓冲的进度条-->
			<div class="player_ctl_progress_bar_buff"></div>
			<!--进度条动态-->
			<div class="player_ctl_progress_bar_move">
				<!--进度条按钮-->
				<div class="player_ctl_progress_bar_btn"></div>
			</div>
		</div>
	</div>

	<!--展厅播放快进快退音量全屏-->
	<div class="player_ctl_bar_list">
		<!--暂停和快进快退-->
		<div class="player_ctl_bar_list_box">
			<i class="glyphicon glyphicon-fast-backward" onClick="fastBackward(event)"  title="关键帧快退回放"></i>&nbsp;&nbsp;&nbsp;&nbsp;
			<i class="glyphicon glyphicon-pause" id="play_btn" onClick="pause(event)" title="暂停"></i>&nbsp;&nbsp;&nbsp;&nbsp;
			<i class="glyphicon glyphicon-forward" onClick="forward(event)" title="快进回放"></i>
			<i class="glyphicon glyphicon-fast-forward" onClick="fastForward(event)" title="关键帧播放"></i>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>

		<!--音量-->
		<div class="player_ctl_bar_list_voice">
			<i class="glyphicon glyphicon-volume-up" style="float: left;"></i>
			<div class="player_ctl_bar_voice_bar_box">
				<div class="player_ctl_bar_voice_bar" id="player_ctl_bar_voice_bar">
					<div class="player_ctl_bar_voice_bar_in" id="player_ctl_bar_voice_bar_in"></div>
				</div>
				<div class="player_ctl_bar_voice_bar_btn" id="player_ctl_bar_voice_bar_btn"></div>
			</div>
		</div>

		<!--时间-->
		<div class="player_video_time" style="display:none;">
			<font id="video_play_time">00:00:00</font>/<em id="video_end_time">00:00:00</em>
		</div>
		<!--全屏-->
		<!-- i id="player_ctl_bar_full_screen" class="glyphicon glyphicon-fullscreen"></i-->
	</div>
</div>
