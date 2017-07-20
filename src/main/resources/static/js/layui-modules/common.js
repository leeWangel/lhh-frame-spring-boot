var eventConf = {
	'DevJoin':'连接',
	'Normal':'正常',
	'NotFound':'未找到',
	'Broken':'损坏',
	'Leaking':'泄漏',
}

function getLocalTime(nS) { 
	return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " "); 
}