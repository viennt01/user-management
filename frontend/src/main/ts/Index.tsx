import * as React from "react";
import { ReactNode } from "react";
import * as ReactDOM from 'react-dom';
import "antd/dist/antd.css";
import { UserTable } from "./UserTable";

class Index extends React.Component {

	constructor(props: {}) {
		super(props);
	}

	render(): ReactNode {
		return (
			<UserTable/>
		)
	}
}

ReactDOM.render(
	<Index/>,
	document.getElementById('root')
);