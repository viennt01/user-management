import * as React from "react";
import { ReactNode } from "react";
import { UserTableState } from "./UserTableState";
import axios, { AxiosResponse } from 'axios';
import { UserResponse } from "./UserResponse";
import { Table } from "antd";
import { CONTEXT_PATH } from "./Constant";

export class UserTable extends React.Component<{}, UserTableState> {

	private columns: any[];

	constructor(props: {}) {
		super(props);
		this.state = {
			users: []
		};

		axios.create().get(CONTEXT_PATH + "/api/users").then((response: AxiosResponse) => {
			const users: UserResponse[] = response.data._embedded.users;
			this.setState({users: users});
		});

		this.columns = [{
			title: 'Username',
			dataIndex: 'username',
			width: '25%'
		}, {
			title: 'Email',
			dataIndex: 'email',
			width: '25%'
		}, {
			title: 'First name',
			dataIndex: 'firstName',
			width: '25%'
		}, {
			title: 'Last name',
			dataIndex: 'lastName',
			width: '25%'
		}];

	}

	render(): ReactNode {
		return (
			<Table dataSource={this.state.users}
				   columns={this.columns}/>
		)
	}


}